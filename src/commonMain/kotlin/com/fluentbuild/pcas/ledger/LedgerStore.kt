package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.ledger.models.*
import com.fluentbuild.pcas.utils.TimeProvider
import com.fluentbuild.pcas.utils.logger
import com.fluentbuild.pcas.utils.mapSet
import com.fluentbuild.pcas.utils.observable

class LedgerStore(
    self: HostInfo,
    onChanged: (Ledger) -> Unit,
    private val timeProvider: TimeProvider
) {

    private val log by logger()
    private var ledger by observable(Ledger(self), onChanged)

    fun get() = ledger

    fun upsertBonds(host: HostInfo, bonds: Set<BondEntity>) {
        upsert(host, bonds, ledger.propEntries.filterByHost(host).mapSet { it.entity })
    }

    fun upsertProps(host: HostInfo, props: Set<PropertyEntity>) {
        upsert(host, ledger.bondEntries.filterByHost(host).mapSet { it.entity }, props)
    }

    fun upsert(host: HostInfo, bonds: Set<BondEntity>, props: Set<PropertyEntity>) {
        log.debug(::upsert, host, bonds, props)

        val timestamp = timeProvider.currentTimeMillis()
        val bondEntries = bonds.mapSet { Entry(it, host, timestamp) }
        val propEntries = props.mapSet { Entry(it, host, timestamp) }

        upsertInternal(bondEntries, propEntries)
    }

    fun updateSelf(newSelf: HostInfo) {
        ledger = ledger.copy(
            bondEntries = ledger.bondEntries + bondEntries,
            propEntries = ledger.propEntries + propEntries
        )
    }

    private fun upsertInternal(bondEntries: Set<Entry<BondEntity>>, propEntries: Set<Entry<PropertyEntity>>) {
        ledger = ledger.copy(
            bondEntries = ledger.bondEntries + bondEntries,
            propEntries = ledger.propEntries + propEntries
        )
    }

    fun evict(host: HostInfo) {
        val bondsToEvict = ledger.bondEntries.filterByHost(host)
        val propsToEvict = ledger.propEntries.filterByHost(host)
        log.info { "Evicting ($host) bonds: $bondsToEvict, props: $propsToEvict" }

        ledger = ledger.copy(
            bondEntries = ledger.bondEntries - bondsToEvict,
            propEntries = ledger.propEntries - propsToEvict,
        )
    }

    fun clear() {
        ledger = Ledger(ledger.self)
    }
}