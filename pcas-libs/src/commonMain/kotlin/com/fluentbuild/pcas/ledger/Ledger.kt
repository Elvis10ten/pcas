package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.conflicts.Conflict
import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.utils.filterSet
import com.fluentbuild.pcas.utils.mapSet

data class Ledger(
    internal val self: HostInfo,
    internal val blocks: Set<Block> = emptySet()
) {

    val selfBlocks = blocks.filterSet { it.owner == self }

    val peersBlocks = blocks.filterSet { it.owner != self }

    val contentions = selfBlocks.mapSet { Conflict(it, it.peersApexContentionBlock) }

    val allHosts = blocks.groupBy { it.owner }

    private inline val Block.peersApexContentionBlock get() = peersBlocks
        .filterSet { this.hasConflict(it) }
        .maxByOrNull { it.rank }
}