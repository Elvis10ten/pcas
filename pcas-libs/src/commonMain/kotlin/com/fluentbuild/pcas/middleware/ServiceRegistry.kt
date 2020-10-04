package com.fluentbuild.pcas.middleware

import com.fluentbuild.pcas.ledger.Block
import com.fluentbuild.pcas.ledger.LedgerProtocol
import com.fluentbuild.pcas.ledger.Ledger
import com.fluentbuild.pcas.services.ServiceId
import com.fluentbuild.pcas.stream.StreamDemux
import com.fluentbuild.pcas.utils.logger

class ServiceRegistry internal constructor(
    private val ledgerProtocol: LedgerProtocol,
    private val streamDemux: StreamDemux,
    private val conflictsResolver: ConflictsResolver,
    private val serviceHandlers: Map<ServiceId, ResolutionHandler>
) {

    private val log by logger()
    private var isInitialized = false

    fun init() {
        require(!isInitialized) { "ServiceRegistry already initialized" }
        log.info { "Initializing" }
        streamDemux.init()
        ledgerProtocol.init(::onLedgerUpdated)
        isInitialized = true
    }

    fun close() {
        log.info { "Closing" }
        serviceHandlers.values.forEach { it.release() }
        streamDemux.close()
        ledgerProtocol.close()
        isInitialized = false
    }

    fun updateBlocks(blocks: Set<Block>) {
        log.info { "Updating blocks: $blocks" }
        ledgerProtocol.updateBlocks(blocks)
    }

    private fun onLedgerUpdated(ledger: Ledger) {
        log.info { "Ledger updated: $ledger" }
        conflictsResolver.resolve(ledger).forEach { resolution ->
            log.info { "Handling resolution: $resolution" }
            serviceHandlers.getValue(resolution.selfBlock.serviceId).handle(resolution)
        }
    }
}