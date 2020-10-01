package com.fluentbuild.pcas.middleware

import com.fluentbuild.pcas.ledger.LedgerProtocol
import com.fluentbuild.pcas.ledger.models.PropertyEntity
import com.fluentbuild.pcas.ledger.models.BondEntity
import com.fluentbuild.pcas.ledger.models.Ledger
import com.fluentbuild.pcas.ledger.models.ServiceId
import com.fluentbuild.pcas.middleware.routing.RouterServerDeMultiplexer
import com.fluentbuild.pcas.utils.logger

class ServiceRegistry(
    private val ledgerProtocol: LedgerProtocol,
    private val routerServerDeMultiplexer: RouterServerDeMultiplexer,
    private val interceptors: List<UpdateInterceptor>,
    private val serviceHandlers: Map<ServiceId, CommandHandler>
) {

    private val log by logger()

    fun init() {
        log.debug(::init)
        routerServerDeMultiplexer.init()
        ledgerProtocol.init(::onLedgerUpdated)
    }

    fun close() {
        log.debug(::close)
        serviceHandlers.values.forEach { it.release() }
        ledgerProtocol.close()
        routerServerDeMultiplexer.close()
    }

    fun updateBonds(bonds: Set<BondEntity>) {
        log.debug(::updateBonds, bonds)
        ledgerProtocol.updateBonds(bonds)
    }

    fun updateProps(props: Set<PropertyEntity>) {
        log.debug(::updateProps, props)
        ledgerProtocol.updateProps(props)
    }

    private fun onLedgerUpdated(ledger: Ledger) {
        log.debug(::onLedgerUpdated, ledger)
        interceptors.forEach { interceptor ->
            interceptor.intercept(ledger).forEach(::handle)
        }
    }

    private fun handle(command: Command) {
        log.debug(::handle, command)
        serviceHandlers.getValue(command.serviceId).handle(command)
    }
}