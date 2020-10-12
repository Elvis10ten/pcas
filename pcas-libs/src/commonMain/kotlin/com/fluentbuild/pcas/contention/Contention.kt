package com.fluentbuild.pcas.contention

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.ledger.Block

data class Contention(
    val selfBlock: Block,
    val peersApexBlock: Block?
) {

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