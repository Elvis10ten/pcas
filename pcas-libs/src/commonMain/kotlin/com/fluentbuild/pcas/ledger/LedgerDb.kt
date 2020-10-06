package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.host.Uuid
import com.fluentbuild.pcas.utils.filterSet
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.utils.mapSet

internal class LedgerDb {

    private val log = getLog()
    private var onLedgerUpdated: (Ledger) -> Unit = {}
    private lateinit var ledger: Ledger

    fun create(self: HostInfo, onLedgerUpdated: (Ledger) -> Unit) {
        log.debug(::create, self)
        this.onLedgerUpdated = onLedgerUpdated
        ledger = Ledger(self)
    }

    fun destroy() {
        log.debug(::destroy)
        onLedgerUpdated = {}
        ledger = Ledger(ledger.self)
    }

    fun getLedger() = ledger

    fun upsert(hostBlocks: Set<Block>) {
        log.debug(::upsert, hostBlocks)
        update(ledger.copy(
            blocks = (ledger.blocks - hostBlocks) + hostBlocks
        ))
    }

    fun delete(hostUuids: Set<Uuid>) {
        log.debug(::delete, hostUuids)
        val blocksWithoutHosts = ledger.blocks.filterSet { !hostUuids.contains(it.owner.uuid) }
        update(ledger.copy(
            blocks = blocksWithoutHosts
        ))
    }

    fun updateSelf(newSelf: HostInfo) {
        log.debug(::updateSelf, newSelf)
        val updatedSelfBlocks = ledger.selfBlocks.mapSet { it.copy(owner = newSelf) }
        update(ledger.copy(
            self = newSelf,
            blocks = (ledger.blocks - ledger.selfBlocks) + updatedSelfBlocks
        ))
    }

    private fun update(newLedger: Ledger) {
        log.debug { "Ledger updated: $newLedger" }
        ledger = newLedger
        onLedgerUpdated(newLedger)
    }
}