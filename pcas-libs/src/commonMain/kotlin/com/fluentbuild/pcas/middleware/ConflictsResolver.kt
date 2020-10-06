package com.fluentbuild.pcas.middleware

import com.fluentbuild.pcas.ledger.Block
import com.fluentbuild.pcas.ledger.Ledger
import com.fluentbuild.pcas.middleware.Conflict.Resolution
import com.fluentbuild.pcas.utils.filterSet
import com.fluentbuild.pcas.utils.mapSet

internal class ConflictsResolver {

    private val resolutionHandlers: Map<ServiceId, ResolutionHandler>,
    fun resolve(ledger: Ledger): Set<Resolution> {
        val conflicts = ledger.selfBlocks.mapSet { Conflict(it, it.getOthersConflict(ledger.othersBlocks)) }
        return resolve(conflicts)
    }

    fun release() {

    }

    private fun resolve(conflicts: Set<Conflict>): Set<Resolution> {
        return conflicts.mapSet { conflict ->
            when {
                conflict.othersBlocks.isEmpty() -> {
                    Resolution.Connect(conflict.selfBlock)
                }
                conflict.selfBlock.rank > conflict.othersApex.rank -> {
                    Resolution.Connect(conflict.selfBlock)
                }
                conflict.selfBlock.rank < conflict.othersApex.rank -> {
                    if(conflict.selfBlock.hasPriority && !conflict.selfBlock.isConnected && conflict.othersApex.isConnected) {
                        Resolution.Stream(conflict.selfBlock, conflict.othersApex.host)
                    } else {
                        Resolution.Disconnect(conflict.selfBlock)
                    }
                }
                conflict.selfBlock.rank == conflict.othersApex.rank -> {
                    Resolution.Ambiguous(conflict.selfBlock)
                }
                else -> throw IllegalStateException("Impossible!")
            }
        }
    }

    private fun Block.getOthersConflict(othersBlocks: Set<Block>): Set<Block> {
        return othersBlocks.filterSet { othersBlock ->
            this.serviceId == othersBlock.serviceId &&
                    this.bondId == othersBlock.bondId &&
                    (othersBlock.hasPriority || othersBlock.isConnected)
        }
    }
}