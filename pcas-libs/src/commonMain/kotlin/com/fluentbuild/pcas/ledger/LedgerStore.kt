package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.ledger.models.*
import com.fluentbuild.pcas.utils.TimeProvider
import com.fluentbuild.pcas.utils.logger
import com.fluentbuild.pcas.utils.mapSet

class LedgerStore(
    private val timeProvider: TimeProvider
) {

    private val log by logger()
    private var onChanged: (Ledger) -> Unit = {}
    private lateinit var ledger: Ledger

    fun setup(owner: HostInfo, onChanged: (Ledger) -> Unit) {
        ledger = Ledger(owner)
        this.onChanged = onChanged
    }

    fun clear() {
        this.onChanged = {}
        ledger = Ledger(ledger.owner)
    }

    fun get() = ledger

    fun upsertBonds(host: HostInfo, bonds: Set<BondEntity>) {
        upsert(host, bonds, ledger.props.filterHost(host).mapToEntities())
    }

    fun upsertProps(host: HostInfo, props: Set<PropertyEntity>) {
        upsert(host, ledger.bonds.filterHost(host).mapToEntities(), props)
    }

    fun upsert(host: HostInfo, bonds: Set<BondEntity>, props: Set<PropertyEntity>) {
        log.debug(::upsert, host, bonds, props)

        val timestamp = timeProvider.currentTimeMillis()
        val bondEntries = bonds.mapSet { Entry(it, host, timestamp) }
        val propEntries = props.mapSet { Entry(it, host, timestamp) }

        upsertInternal(bondEntries, propEntries)
    }

    fun updateSelf(newSelf: HostInfo) {
        val timestamp = timeProvider.currentTimeMillis()
        val selfBonds = ledger.ownerBonds.map { Entry(it.entity, newSelf, timestamp) }
        val selfProps = ledger.ownerProps.map { Entry(it.entity, newSelf, timestamp) }

        update(ledger.copy(
            owner = newSelf,
            bonds = (ledger.bonds - ledger.ownerBonds) + selfBonds,
            props = (ledger.props - ledger.ownerProps) + selfProps
        ))
    }

    private fun upsertInternal(bondEntries: Set<Entry<BondEntity>>, propEntries: Set<Entry<PropertyEntity>>) {
        update(ledger.copy(
            bonds = ledger.bonds + bondEntries,
            props = ledger.props + propEntries
        ))
    }

    fun evict(host: HostInfo) {
        val bondsToEvict = ledger.bonds.filterHost(host)
        val propsToEvict = ledger.props.filterHost(host)
        log.info { "Evicting ($host) bonds: $bondsToEvict, props: $propsToEvict" }

        update(ledger.copy(
            bonds = ledger.bonds - bondsToEvict,
            props = ledger.props - propsToEvict,
        ))
    }

    private fun update(newLedger: Ledger) {
        val oldLedger = ledger
        ledger = newLedger

        // todo: equals doesn't work like we expect
        if(oldLedger != newLedger) {
            onChanged(newLedger)
        }
    }
}