package com.fluentbuild.pcas.contention

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.ledger.Block

data class Contention(
    val selfBlock: Block,
    val peersApexBlock: Block?
) {

    sealed class Resolution {

        abstract val selfBlock: Block

        abstract val rank: Double

        data class Connect(
            override val selfBlock: Block,
            override val rank: Double
        ): Resolution()

        data class Disconnect(
            override val selfBlock: Block,
            override val rank: Double
        ): Resolution()

        data class Ambiguous(
            override val selfBlock: Block,
            override val rank: Double
        ): Resolution()

        data class Stream(
            override val selfBlock: Block,
            override val rank: Double,
            val destination: HostInfo
        ): Resolution()

        fun clone(selfBlock: Block = this.selfBlock, rank: Double = this.rank): Resolution {
            return when(this) {
                is Connect -> Connect(selfBlock, rank)
                is Disconnect -> Disconnect(selfBlock, rank)
                is Ambiguous -> Ambiguous(selfBlock, rank)
                is Stream -> Stream(selfBlock, rank, destination)
            }
        }
    }
}