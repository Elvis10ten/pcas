package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.host.HostUuid
import com.fluentbuild.pcas.utils.filterSet
import com.fluentbuild.pcas.utils.logger
import com.fluentbuild.pcas.utils.mapSet

internal class LedgerDb {

    private val log by logger()
    private var onLedgerUpdated: (Ledger) -> Unit = {}
    private lateinit var ledger: Ledger

    fun create(self: HostInfo, onLedgerUpdated: (Ledger) -> Unit) {
        this.onLedgerUpdated = onLedgerUpdated
        ledger = Ledger(self)
    }

    fun destroy() {
        onLedgerUpdated = {}
        ledger = Ledger(ledger.self)
    }

    fun getLedger() = ledger

    fun upsert(hostBlocks: Set<Block>) {
        update(ledger.copy(
            blocks = (ledger.blocks - hostBlocks) + hostBlocks
        ))
    }

    fun delete(hostUuids: Set<HostUuid>) {
        val blocksWithoutHosts = ledger.blocks.filterSet { !hostUuids.contains(it.host.uuid) }
        update(ledger.copy(
            blocks = blocksWithoutHosts
        ))
    }

    fun updateSelf(newSelf: HostInfo) {
        val updatedSelfBlocks = ledger.selfBlocks.mapSet { it.copy(host = newSelf) }
        update(ledger.copy(
            self = newSelf,
            blocks = (ledger.blocks - ledger.selfBlocks) + updatedSelfBlocks
        ))
    }

    private fun update(newLedger: Ledger) {
        log.info { "Ledger updated: $newLedger" }
        ledger = newLedger
        onLedgerUpdated(newLedger)
    }
}