package com.fluentbuild.pcas.ledger.messages

import com.fluentbuild.pcas.io.MarshalledMessage
import com.fluentbuild.pcas.io.MarshalledMessageSize
import com.fluentbuild.pcas.io.MessageReceiver
import com.fluentbuild.pcas.ledger.LedgerDb
import com.fluentbuild.pcas.ledger.LedgerWatchdog
import com.fluentbuild.pcas.ledger.getBlocksMaxTimestamp
import com.fluentbuild.pcas.utils.decode
import com.fluentbuild.pcas.logs.getLog
import kotlinx.serialization.protobuf.ProtoBuf

internal class MessageReceiver(
    private val protoBuf: ProtoBuf,
    private val messageSender: MessageSender,
    private val ledgerDb: LedgerDb,
    private val watchdog: LedgerWatchdog
): MessageReceiver {

    private val log = getLog()
    private val ledger get() = ledgerDb.getLedger()

    override fun onReceived(message: MarshalledMessage, size: MarshalledMessageSize) {
        onReceived(protoBuf.decode(message, size))
    }

    private fun onReceived(message: LedgerMessage) {
        log.debug { "Received: $message" }
        log.info { "Received: ${message::class.simpleName}" }

        if(message is LedgerMessage.Essential) {
            messageSender.sendAcknowledgement(message.sender, message.sequenceNumber)
        }

        when(message) {
            is LedgerMessage.Essential.Genesis -> {
                messageSender.sendUpdate()
            }
            is LedgerMessage.Essential.Update -> {
                ledgerDb.upsert(message.senderBlocks)
            }
            is LedgerMessage.InEssential.Heartbeat -> {
                watchdog.onHeartbeatReceived(message.sender)
                if(message.isSelfBlocksOnSenderStale()) {
                    log.warn { "Self blocks STALE on message sender" }
                    messageSender.sendUpdate()
                }
            }
            is LedgerMessage.InEssential.Ack -> {
                messageSender.onAcknowledgmentReceived(message)
            }
        }
    }

    private fun LedgerMessage.InEssential.Heartbeat.isSelfBlocksOnSenderStale(): Boolean {
        val selfBlocksOnLocalMaxTimestamp = ledger.selfBlocks.getBlocksMaxTimestamp()
        val selfBlocksOnSenderMaxTimestamp = hostBlocksMaxTimestamp[ledger.self.uuid] ?: return true
        return selfBlocksOnLocalMaxTimestamp > selfBlocksOnSenderMaxTimestamp
    }
}