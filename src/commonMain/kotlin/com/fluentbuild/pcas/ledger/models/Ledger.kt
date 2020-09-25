package com.fluentbuild.pcas.ledger.models

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.ledger.filterByHost
import com.fluentbuild.pcas.ledger.filterByNotHost
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
    val owner: HostInfo,
    @ProtoId(2)
    val bonds: Set<Entry<BondEntity>> = emptySet(),
    @ProtoId(3)
    val props: Set<Entry<PropertyEntity>> = emptySet()
) {

    @Transient
    val ownerBonds by unsafeLazy { bonds.filterByHost(owner) }

    @Transient
    val othersBonds by unsafeLazy { bonds.filterByNotHost(owner) }

    @Transient
    val ownerProps by unsafeLazy { props.filterByHost(owner) }

    @Transient
    val othersProps by unsafeLazy { props.filterByNotHost(owner) }

    fun hasBond(prop: Entry<PropertyEntity>) = bonds.filterSet { it == prop }.isNotEmpty()

    fun getEvictionNotices(currentTimestamp: Long): Set<HostInfo> {
        val evictionNoticeThreshold = ENTRY_EVICTION_NOTICE_THRESHOLD_MILLIS
        val evictionFromBonds = bonds.filterSet { currentTimestamp - it.timestamp > evictionNoticeThreshold }
        val evictionFromProps = props.filterSet { currentTimestamp - it.timestamp > evictionNoticeThreshold }
        return (evictionFromBonds + evictionFromProps).mapSet { it.host }
    }

    companion object {
        const val ENTRY_TTL_MILLIS = 32 * 1000
        const val ENTRY_EVICTION_NOTICE_THRESHOLD_MILLIS = ENTRY_TTL_MILLIS / 2
    }
}