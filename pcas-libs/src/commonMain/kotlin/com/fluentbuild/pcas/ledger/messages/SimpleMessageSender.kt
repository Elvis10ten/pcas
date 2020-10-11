package com.fluentbuild.pcas.ledger.messages

import com.fluentbuild.pcas.async.ThreadRunner
import com.fluentbuild.pcas.Uuid
import com.fluentbuild.pcas.io.MarshalledMessage
import com.fluentbuild.pcas.io.MarshalledMessageSize
import com.fluentbuild.pcas.io.SecureMulticastChannel
import com.fluentbuild.pcas.ledger.LedgerDb
import com.fluentbuild.pcas.logs.getLog
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import kotlin.math.pow
import kotlin.random.Random

internal class SimpleMessageSender(
	private val protoBuf: ProtoBuf,
	private val runner: ThreadRunner,
	private val random: Random,
	private val multicast: SecureMulticastChannel,
	private val ledgerDb: LedgerDb
): MessageSender(ledgerDb) {

	private val log = getLog()
	private val ledger get() = ledgerDb.getLedger()

	private var numRetries = 0
	private var currentSequenceNumber = 0
	private val acknowledgements = mutableSetOf<Uuid>()
	private var expectedNumberOfAcknowledgements = 0

	override fun send(message: LedgerMessage) {
		log.debug { "Sending message: $message" }
		log.info { "Sending message: ${message::class.simpleName}" }
		val marshalledMessage = protoBuf.encodeToByteArray(message)

		if(message is LedgerMessage.Essential) {
			sendReliably(marshalledMessage, marshalledMessage.size, message.sequenceNumber)
		} else {
			multicast.send(marshalledMessage, marshalledMessage.size)
		}
	}

	private fun sendReliably(message: MarshalledMessage, messageSize: MarshalledMessageSize, sequenceNumber: Int) {
		clear()
		currentSequenceNumber = sequenceNumber
		// By design: There is usually at least one host
		expectedNumberOfAcknowledgements = ledger.allHosts.size
		log.info { "Current SeqNum: $currentSequenceNumber, Expected ACKs: $expectedNumberOfAcknowledgements" }

		multicast.send(message, messageSize)

		runner.runOnMainDelayed(getRetryDelay(), object: Function0<Unit> {

			override fun invoke() {
				numRetries++
				log.warn { "Retrying last message... (count = $numRetries)" }
				multicast.send(message, messageSize)

				if(!isAcknowledgementsComplete() && canRetry()) {
					runner.runOnMainDelayed(getRetryDelay(), this)
				}
			}
		})
	}

	override fun onAcknowledgmentReceived(message: LedgerMessage.InEssential.Ack) {
		with(message) {
			if(ackTarget == ledger.self.uuid && ackSequenceNumber == currentSequenceNumber) {
				log.info { "Message ACKed(sequenceNumber = ${message.ackSequenceNumber})" }
				acknowledgements.add(sender)

				if(isAcknowledgementsComplete()) {
					clear()
				}
			} else {
				log.warn { "Invalid ACK: $message" }
			}
		}
	}

	private fun clear() {
		runner.cancelAll()
		acknowledgements.clear()
		numRetries = 0
	}

	override fun cancel() {
		clear()
		currentSequenceNumber = 0
		expectedNumberOfAcknowledgements = 0
	}

	override fun getNextSequenceNumber() = currentSequenceNumber + 1

	private fun getRetryDelay() = ((INITIAL_DELAY_MILLIS * BASE.pow(numRetries)) + getJitter()).toInt()

	private fun getJitter() = random.nextInt(MAX_JITTER_MILLIS)

	private fun canRetry() = numRetries < MAX_RETRIES

	private fun isAcknowledgementsComplete() = acknowledgements.size >= expectedNumberOfAcknowledgements

	companion object {

		private const val INITIAL_DELAY_MILLIS = 50
		private const val BASE = 2.0
		private const val MAX_JITTER_MILLIS = 10
		private const val MAX_RETRIES = 7
	}
}