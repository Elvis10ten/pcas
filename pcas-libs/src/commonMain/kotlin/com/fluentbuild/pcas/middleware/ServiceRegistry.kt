package com.fluentbuild.pcas.middleware

import com.fluentbuild.pcas.ledger.LedgerProtocol
import com.fluentbuild.pcas.ledger.models.PropertyEntity
import com.fluentbuild.pcas.ledger.models.BondEntity
import com.fluentbuild.pcas.ledger.Ledger
import com.fluentbuild.pcas.services.ServiceId
import com.fluentbuild.pcas.routing.RouterServerDeMultiplexer
import com.fluentbuild.pcas.utils.logger

class ServiceRegistry internal constructor(
    private val ledgerProtocol: LedgerProtocol,
    private val routerDeMultiplexer: RouterServerDeMultiplexer,
    private val interceptors: List<UpdateInterceptor>,
    private val serviceHandlers: Map<ServiceId, CommandHandler>
) {

    private val log by logger()

    fun init() {
        log.info { "Initializing" }
        routerDeMultiplexer.init()
        ledgerProtocol.init(::onLedgerUpdated)
    }

    fun close() {
        log.info { "Closing" }
        serviceHandlers.values.forEach { it.release() }
        routerDeMultiplexer.close()
        ledgerProtocol.close()
    }

    fun updateBonds(bonds: Set<BondEntity>) {
        log.info { "Updating bonds: $bonds" }
        ledgerProtocol.updateBonds(bonds)
    }

    fun updateProps(props: Set<PropertyEntity>) {
        log.info { "Updating props: $props" }
        ledgerProtocol.updateProps(props)
    }

    private fun onLedgerUpdated(ledger: Ledger) {
        log.info { "Ledger updated: $ledger" }
        interceptors.forEach { handle(it.intercept(ledger)) }
    }

    private fun handle(commands: Set<Command>) {
        commands.forEach { command ->
            log.info { "Handling command: $command" }
            serviceHandlers.getValue(command.serviceId).handle(command)
        }
    }
}