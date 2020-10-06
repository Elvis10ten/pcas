package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.io.MulticastChannel
import com.fluentbuild.pcas.logs.getLog
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

internal class LedgerMessageSender(
    private val protoBuf: ProtoBuf,
    private val multicast: MulticastChannel,
    private val ledgerDb: LedgerDb
) {

    private val log = getLog()
    private val ledger get() = ledgerDb.getLedger()

    fun sendGenesis() {
        broadcast(LedgerMessage.Genesis(ledger.self.uuid))
    }

    fun sendExodus() {
        broadcast(LedgerMessage.Exodus(ledger.self.uuid))
    }

    fun sendUpdate() {
        broadcast(LedgerMessage.Update(ledger.self.uuid, ledger.selfBlocks))
    }

    fun sendHeartbeat() {
        val hostBlocksMaxTimestamps = ledger.blocks
            .groupBy { it.owner.uuid }
            .mapValues { it.value.getBlocksMaxTimestamp() }
        broadcast(LedgerMessage.Heartbeat(ledger.self.uuid, hostBlocksMaxTimestamps))
    }

    private fun broadcast(message: LedgerMessage) {
        log.info { "Broadcasting message: $message" }
        val marshalledMessage = protoBuf.encodeToByteArray(message)
        multicast.broadcast(marshalledMessage, marshalledMessage.size)
    }
}