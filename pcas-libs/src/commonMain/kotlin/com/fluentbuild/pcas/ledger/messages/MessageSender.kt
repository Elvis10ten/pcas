package com.fluentbuild.pcas.ledger.messages

import com.fluentbuild.pcas.host.Uuid
import com.fluentbuild.pcas.ledger.LedgerDb
import com.fluentbuild.pcas.ledger.getBlocksMaxTimestamp

internal abstract class MessageSender(
    private val ledgerDb: LedgerDb
) {

    private val ledger get() = ledgerDb.getLedger()
    private val senderUuid get() = ledger.self.uuid

    fun sendGenesis() {
        send(LedgerMessage.InEssential.Genesis(senderUuid))
    }

    fun sendUpdate() {
        send(LedgerMessage.Essential.Update(senderUuid, ledger.selfBlocks, getNextSequenceNumber()))
    }

    fun sendHeartbeat() {
        val hostBlocksMaxTimestamp = ledger.blocks
            .groupBy { it.owner.uuid }
            .mapValues { it.value.getBlocksMaxTimestamp() }

        send(LedgerMessage.InEssential.Heartbeat(senderUuid, hostBlocksMaxTimestamp))
    }

    fun sendAcknowledgement(ackTarget: Uuid, ackSequenceNumber: Int) {
        send(LedgerMessage.InEssential.Ack(senderUuid, ackTarget, ackSequenceNumber))
    }

    protected abstract fun getNextSequenceNumber(): Int

    protected abstract fun send(message: LedgerMessage)

    abstract fun onAcknowledgmentReceived(message: LedgerMessage.InEssential.Ack)

    abstract fun cancel()
}