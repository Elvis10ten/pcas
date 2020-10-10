package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.host.Uuid
import com.fluentbuild.pcas.utils.filterSet
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.utils.mapSet
import com.fluentbuild.pcas.utils.upsert

internal class LedgerDb {

    private val log = getLog()
    private var onLedgerUpdated: ((Ledger) -> Unit)? = null
    private lateinit var ledger: Ledger

    fun create(self: HostInfo, onLedgerUpdated: (Ledger) -> Unit) {
        log.debug(::create, self)
        this.onLedgerUpdated = onLedgerUpdated
        ledger = Ledger(self)
    }

    fun destroy() {
        log.debug(::destroy)
        onLedgerUpdated = null
        ledger = Ledger(ledger.self)
    }

    fun getLedger() = ledger

    fun upsert(hostBlocks: Set<Block>) {
        log.debug(::upsert, hostBlocks)
        val updateBlocks = ledger.blocks.upsert(hostBlocks)
        update(ledger.copy(blocks = updateBlocks))
    }

    fun delete(hostUuids: Set<Uuid>) {
        log.debug(::delete, hostUuids)
        val blocksWithoutHosts = ledger.blocks.filterSet { !hostUuids.contains(it.owner.uuid) }
        update(ledger.copy(blocks = blocksWithoutHosts))
    }

    fun updateSelf(newSelf: HostInfo) {
        log.debug(::updateSelf, newSelf)
        val updatedSelfBlocks = ledger.selfBlocks.mapSet { it.copy(owner = newSelf) }
        val updatedBlocks = ledger.blocks.upsert(updatedSelfBlocks)
        update(ledger.copy(self = newSelf, blocks = updatedBlocks))
    }

    private fun update(newLedger: Ledger) {
        log.debug(::update, newLedger)
        ledger = newLedger
        onLedgerUpdated?.invoke(newLedger)
    }
}