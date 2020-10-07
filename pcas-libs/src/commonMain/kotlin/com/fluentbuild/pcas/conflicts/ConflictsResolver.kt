package com.fluentbuild.pcas.conflicts

import com.fluentbuild.pcas.ledger.Block
import com.fluentbuild.pcas.ledger.Ledger
import com.fluentbuild.pcas.conflicts.Conflict.Resolution
import com.fluentbuild.pcas.services.ServiceId
import com.fluentbuild.pcas.services.audio.AudioResolutionHandler
import com.fluentbuild.pcas.utils.TimeProvider
import com.fluentbuild.pcas.utils.filterSet
import com.fluentbuild.pcas.utils.mapSet

internal class ConflictsResolver(
    private val timeProvider: TimeProvider
) {

    private val resolutionHandlers: Map<ServiceId, ResolutionHandler>,
    fun resolve(ledger: Ledger): Set<Resolution> {
        if(!canAttempt(resolution)) {
            log.warn { "Ran too often. Ignoring resolution: $resolution" }
            return
        }

        val currentTimestamp = timeProvider.currentTimeMillis()
        lastResolutionTimestamp = currentTimestamp
        congruentAction.count(resolution) { currentTimestamp - lastResolutionTimestamp < AudioResolutionHandler.ADJACENT_ACTION_WINDOW_MILLIS }

        private val congruentAction = SoloConsecutiveCounter<Conflict.Resolution>()
        private var lastResolutionTimestamp = 0L

        val conflicts = ledger.selfBlocks.mapSet { Conflict(it, it.getOthersConflict(ledger.othersBlocks)) }
        return resolve(conflicts)
    }


    private fun canAttempt(currentResolution: Conflict.Resolution) =
        congruentAction[currentResolution] >= AudioResolutionHandler.MAX_NUM_CONGRUENT_ADJACENT_ACTIONS

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
                        Resolution.Stream(conflict.selfBlock, conflict.othersApex.owner)
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

    companion object {

        const val ADJACENT_ACTION_WINDOW_MILLIS = 20 * 1000
        const val MAX_NUM_CONGRUENT_ADJACENT_ACTIONS = 5
    }
}