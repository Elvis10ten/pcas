package com.fluentbuild.pcas.ledger.models

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.ledger.filterHost
import com.fluentbuild.pcas.ledger.filterNotHost
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
    val ownerBonds by unsafeLazy { bonds.filterHost(owner) }

    @Transient
    val othersBonds by unsafeLazy { bonds.filterNotHost(owner) }

    @Transient
    val ownerProps by unsafeLazy { props.filterHost(owner) }

    @Transient
    val othersProps by unsafeLazy { props.filterNotHost(owner) }

    fun hasBond(prop: Entry<PropertyEntity>) = bonds.filterSet { it == prop }.isNotEmpty()

    fun getEvictionNotices(currentTimestamp: Long): Set<HostInfo> {
        val evictionNoticeThreshold = ENTRY_EVICTION_NOTICE_THRESHOLD_MILLIS
        val evictionFromBonds = bonds.filterSet { currentTimestamp - it.timestamp > evictionNoticeThreshold }
        val evictionFromProps = props.filterSet { currentTimestamp - it.timestamp > evictionNoticeThreshold }
        return (evictionFromBonds + evictionFromProps)
            .filterSet { it.host != owner }
            .mapSet { it.host }
    }

    companion object {
        const val ENTRY_EVICTION_NOTICE_THRESHOLD_MILLIS = 30
    }
}