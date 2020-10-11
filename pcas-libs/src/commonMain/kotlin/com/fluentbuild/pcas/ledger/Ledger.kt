package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.contention.Contention
import com.fluentbuild.pcas.HostInfo
import com.fluentbuild.pcas.utils.filterSet
import com.fluentbuild.pcas.utils.mapSet

data class Ledger(
	internal val self: HostInfo,
	internal val blocks: Set<Block> = emptySet()
) {

    val selfBlocks = blocks.filterSet { it.owner == self }

    val peersBlocks = blocks.filterSet { it.owner != self }

    val contentions = selfBlocks.mapSet { Contention(it, it.peersApexContentionBlock) }

    val allPeers = peersBlocks.groupBy { it.owner }

    private inline val Block.peersApexContentionBlock get() = peersBlocks
        .filterSet { this.hasContention(it) }
        .maxByOrNull { it.rank }
}