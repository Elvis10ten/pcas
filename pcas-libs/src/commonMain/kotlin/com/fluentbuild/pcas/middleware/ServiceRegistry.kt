package com.fluentbuild.pcas.middleware

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.ledger.LedgerProtocol
import com.fluentbuild.pcas.ledger.Ledger
import com.fluentbuild.pcas.services.ServiceId
import com.fluentbuild.pcas.stream.StreamDemux
import com.fluentbuild.pcas.logs.getLog

class ServiceRegistry internal constructor(
    private val ledgerProtocol: LedgerProtocol,
    private val streamDemux: StreamDemux,
    private val conflictsResolver: ConflictsResolver,
    private val serviceHandlers: Map<ServiceId, ResolutionHandler>
) {

    private val log = getLog()

    fun run(): Cancellable {
        log.info { "Initializing" }
        streamDemux.init()
        ledgerProtocol.init(::onLedgerUpdated)

        return Cancellable {
            log.info { "Closing" }
            serviceHandlers.values.forEach { it.release() }
            streamDemux.close()
            ledgerProtocol.close()
        }
    }

    private fun onLedgerUpdated(ledger: Ledger) {
        //log.info { "Ledger updated: $ledger" }
        conflictsResolver.resolve(ledger).forEach { resolution ->
            log.info { "Handling resolution: $resolution" }
            serviceHandlers.getValue(resolution.selfBlock.serviceId).handle(resolution)
        }
    }
}