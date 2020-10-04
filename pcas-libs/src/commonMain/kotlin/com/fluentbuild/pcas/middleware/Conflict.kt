package com.fluentbuild.pcas.middleware

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.ledger.Block
import com.fluentbuild.pcas.utils.unsafeLazy

data class Conflict(
    val selfBlock: Block,
    val othersBlocks: Set<Block>
) {

    val othersApex by unsafeLazy { othersBlocks.maxByOrNull { it.rank }!! }

    sealed class Resolution {

        abstract val selfBlock: Block

        data class Connect(override val selfBlock: Block): Resolution()

        data class Disconnect(override val selfBlock: Block): Resolution()

        data class Ambiguous(override val selfBlock: Block): Resolution()

        data class Stream(
            override val selfBlock: Block,
            val destination: HostInfo
        ): Resolution()
    }
}