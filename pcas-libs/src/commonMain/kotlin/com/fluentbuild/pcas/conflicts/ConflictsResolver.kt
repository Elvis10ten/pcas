package com.fluentbuild.pcas.conflicts

import com.fluentbuild.pcas.conflicts.Conflict.Resolution
import com.fluentbuild.pcas.ledger.Ledger
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.services.ServiceId
import com.fluentbuild.pcas.async.Debouncer
import com.fluentbuild.pcas.utils.mapSet

internal class ConflictsResolver(
    private val resolutionHandlers: Map<ServiceId, ResolutionHandler>,
    private val throttle: Throttle,
    private val debouncer: Debouncer
) {

    private val log = getLog()

    fun resolve(ledger: Ledger) {
        log.info { "Resolving conflict" }
        val resolutions = ledger.conflicts.getResolutions()
        debouncer.debounce { resolutions.handle() }
    }

    private fun Set<Conflict>.getResolutions(): Set<Resolution> {
        log.debug { "Self blocks conflict: $this" }
        return mapSet { conflict ->
            log.info { "RankSelf(${conflict.selfBlock.rank}) vs RankApex(${conflict.peersApexBlock?.rank})" }
            when {
                conflict.peersApexBlock == null -> {
                    Resolution.Connect(conflict.selfBlock)
                }
                conflict.selfBlock.rank > conflict.peersApexBlock.rank -> {
                    Resolution.Connect(conflict.selfBlock)
                }
                conflict.selfBlock.rank < conflict.peersApexBlock.rank -> {
                    if(conflict.canStream()) {
                        Resolution.Stream(conflict.selfBlock, conflict.peersApexBlock.owner)
                    } else {
                        Resolution.Disconnect(conflict.selfBlock)
                    }
                }
                conflict.selfBlock.rank == conflict.peersApexBlock.rank -> {
                    Resolution.Ambiguous(conflict.selfBlock)
                }
                else -> throw IllegalStateException("Impossible!")
            }
        }
    }

    private fun Set<Resolution>.handle() {
        forEach { resolution ->
            if(throttle.record(resolution)) {
                log.info { "Handling resolution: $resolution" }
                resolutionHandlers.getValue(resolution.selfBlock.serviceId).handle(resolution)
            } else {
                log.warn { "Ignoring resolution burst: $resolution" }
            }
        }
    }

    fun release() {
        debouncer.cancel()
        throttle.reset()
        resolutionHandlers.values.forEach { it.release() }
    }

    private fun Conflict.canStream() =
        selfBlock.hasPriority && !selfBlock.isConnected && peersApexBlock?.isConnected == true
}