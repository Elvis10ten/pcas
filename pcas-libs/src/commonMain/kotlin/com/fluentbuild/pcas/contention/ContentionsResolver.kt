package com.fluentbuild.pcas.contention

import com.fluentbuild.pcas.contention.Contention.Resolution
import com.fluentbuild.pcas.ledger.Ledger
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.services.ServiceId
import com.fluentbuild.pcas.utils.mapSet

internal class ContentionsResolver(
    private val resolutionHandlers: Map<ServiceId, ResolutionHandler>,
    private val throttler: ResolutionThrottler
) {

    private val log = getLog()

    fun resolve(ledger: Ledger) {
        log.info { "Resolving contentions" }
        ledger.contentions.getResolutions().forEach {
            throttler.throttle(it, ::handle)
        }
    }

    private fun Set<Contention>.getResolutions(): Set<Resolution> {
        log.debug { "Self blocks contentions: $this" }
        return mapSet { contention ->
            log.info { "SelfRank(${contention.selfBlock.rank}) vs PeersApexRank(${contention.peersApexBlock?.rank})" }
            when {
                contention.peersApexBlock == null -> {
                    Resolution.Connect(contention.selfBlock)
                }
                contention.selfBlock.rank > contention.peersApexBlock.rank -> {
                    Resolution.Connect(contention.selfBlock)
                }
                contention.selfBlock.rank < contention.peersApexBlock.rank -> {
                    if(contention.shouldStreamToApex()) {
                        Resolution.Stream(contention.selfBlock, contention.peersApexBlock.owner)
                    } else {
                        Resolution.Disconnect(contention.selfBlock)
                    }
                }
                contention.selfBlock.rank == contention.peersApexBlock.rank -> {
                    Resolution.Ambiguous(contention.selfBlock)
                }
                else -> throw IllegalStateException("Impossible!")
            }
        }
    }

    private fun handle(resolution: Resolution) {
        resolutionHandlers.getValue(resolution.selfBlock.serviceId).handle(resolution)
    }

    fun release() {
        throttler.reset()
        resolutionHandlers.values.forEach { it.release() }
    }

    private fun Contention.shouldStreamToApex() =
        selfBlock.canStream &&
                peersApexBlock?.canStream == true &&
                selfBlock.hasPriority &&
                !selfBlock.isConnected &&
                peersApexBlock.isConnected
}