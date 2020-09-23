package com.fluentbuild.pcas.ledger.models

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.utils.filterSet
import com.fluentbuild.pcas.utils.mapSet
import com.fluentbuild.pcas.utils.unsafeLazy
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.protobuf.ProtoId

@Suppress("TRANSIENT_IS_REDUNDANT")
@Serializable
data class Ledger(
    @ProtoId(1)
    val self: HostInfo,
    @ProtoId(2)
    val bondEntries: Set<Entry<BondEntity>> = emptySet(),
    @ProtoId(3)
    val propEntries: Set<Entry<PropertyEntity>> = emptySet()
) {

    @Transient
    val selfBondEntries by unsafeLazy { bondEntries.filterByHost(self) }

    @Transient
    val othersBondEntries by unsafeLazy { bondEntries.filterByNotHost(self) }

    @Transient
    val selfPropEntries by unsafeLazy { propEntries.filterByHost(self) }

    @Transient
    val othersPropEntries by unsafeLazy { propEntries.filterByNotHost(self) }

    fun hasConnection(propEntry: Entry<PropertyEntity>) =
        bondEntries.filterByHost(propEntry.host).isNotEmpty()

    fun getEvictionNotices(currentTimestamp: Long): Set<HostInfo> {
        val evictionFromBonds =  bondEntries.filterSet { currentTimestamp - it.entryTimestamp > ENTRY_TTL_MILLIS }
            .mapSet { it.host }
        val evictionFromProps =  propEntries.filterSet { currentTimestamp - it.entryTimestamp > ENTRY_TTL_MILLIS }
            .mapSet { it.host }
        return evictionFromBonds + evictionFromProps
    }

    companion object {
        const val ENTRY_TTL_MILLIS = 20 * 1000
    }
}