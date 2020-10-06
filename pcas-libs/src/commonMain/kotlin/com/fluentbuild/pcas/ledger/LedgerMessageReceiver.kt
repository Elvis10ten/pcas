package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.io.MessageReceiver
import com.fluentbuild.pcas.utils.decodeFromByteArray
import com.fluentbuild.pcas.logs.getLog
import kotlinx.serialization.protobuf.ProtoBuf

internal class LedgerMessageReceiver(
    private val protoBuf: ProtoBuf,
    private val messageSender: LedgerMessageSender,
    private val ledgerDb: LedgerDb,
    private val watchdog: LedgerWatchdog
): MessageReceiver {

    private val log = getLog()
    private val ledger get() = ledgerDb.getLedger()

    override fun onReceived(marshalledMessage: ByteArray, actualSize: Int) {
        onReceived(protoBuf.decodeFromByteArray(marshalledMessage, actualSize))
    }

    private fun onReceived(message: LedgerMessage) {
        log.info { "Received message: $message" }

        when(message) {
            is LedgerMessage.Genesis -> {
                messageSender.sendUpdate()
            }
            is LedgerMessage.Update -> {
                if(message.validateSenderBlocks()) {
                    ledgerDb.upsert(message.senderBlocks)
                } else {
                    log.warn { "Sender blocks is invalid" }
                }
            }
            is LedgerMessage.Heartbeat -> {
                watchdog.onHostHeartbeatReceived(message.sender)
                if(message.isSelfBlocksOnSenderStale()) {
                    messageSender.sendUpdate()
                }
            }
            is LedgerMessage.Exodus -> {
                ledgerDb.delete(setOf(message.sender))
            }
        }
    }

    private fun LedgerMessage.Update.validateSenderBlocks() =
        senderBlocks.isNotEmpty() && senderBlocks.all { it.owner.uuid == sender }

    private fun LedgerMessage.Heartbeat.isSelfBlocksOnSenderStale(): Boolean {
        val selfBlocksOnLocalMaxTimestamp = ledger.selfBlocks.getBlocksMaxTimestamp()
        val selfBlocksOnSenderMaxTimestamp = hostBlocksMaxTimestamps[ledger.self.uuid] ?: return true
        return selfBlocksOnLocalMaxTimestamp > selfBlocksOnSenderMaxTimestamp
    }
}