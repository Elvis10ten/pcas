package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.io.MarshalledMessage
import com.fluentbuild.pcas.io.MarshalledMessageSize
import com.fluentbuild.pcas.io.MessageReceiver
import com.fluentbuild.pcas.utils.decode
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

    override fun onReceived(message: MarshalledMessage, size: MarshalledMessageSize) {
        onReceived(protoBuf.decode(message, size))
    }

    private fun onReceived(message: LedgerMessage) {
        log.info { "Received message: $message" }

        when(message) {
            is LedgerMessage.Genesis -> {
                messageSender.sendUpdate()
            }
            is LedgerMessage.Update -> {
                ledgerDb.upsert(message.senderBlocks)
            }
            is LedgerMessage.Heartbeat -> {
                watchdog.onHeartbeatReceived(message.sender)
                if(message.isSelfBlocksOnSenderStale()) {
                    log.warn { "Self blocks stale on message sender" }
                    messageSender.sendUpdate()
                }
            }
        }
    }

    private fun LedgerMessage.Heartbeat.isSelfBlocksOnSenderStale(): Boolean {
        val selfBlocksOnLocalMaxTimestamp = ledger.selfBlocks.getBlocksMaxTimestamp()
        val selfBlocksOnSenderMaxTimestamp = hostBlocksMaxTimestamp[ledger.self.uuid] ?: return true
        return selfBlocksOnLocalMaxTimestamp > selfBlocksOnSenderMaxTimestamp
    }
}