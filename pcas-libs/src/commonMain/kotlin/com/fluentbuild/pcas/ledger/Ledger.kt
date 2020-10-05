package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.utils.filterSet
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.protobuf.ProtoNumber

@Suppress("TRANSIENT_IS_REDUNDANT")
@Serializable
data class Ledger(
    @ProtoNumber(1)
    val self: HostInfo,
    @ProtoNumber(2)
    val blocks: Set<Block> = emptySet()
) {

    @Transient
    val selfBlocks = blocks.filterSet { it.host == self }

    @Transient
    val othersBlocks = blocks.filterSet { it.host != self }
}