package com.fluentbuild.pcas.middleware

import com.fluentbuild.pcas.ledger.LedgerProtocol
import com.fluentbuild.pcas.ledger.models.PropertyEntity
import com.fluentbuild.pcas.ledger.models.BondEntity
import com.fluentbuild.pcas.ledger.models.Ledger
import com.fluentbuild.pcas.ledger.models.ServiceId
import com.fluentbuild.pcas.middleware.resolvers.ConflictsSolver
import com.fluentbuild.pcas.middleware.resolvers.Resolution
import com.fluentbuild.pcas.utils.logger

class ServiceRegistry(
    private val ledgerProtocol: LedgerProtocol,
    private val conflictsSolver: ConflictsSolver,
    private val serviceHandlers: Map<ServiceId, ResolutionHandler>
) {

    private val log by logger()

    fun init() {
        log.debug(::init)
        ledgerProtocol.init(::onLedgerUpdated)
    }

    fun close() {
        log.debug(::close)
        serviceHandlers.values.forEach { it.release() }
        ledgerProtocol.close()
    }

    fun updateConnections(bonds: Set<BondEntity>) {
        log.debug(::updateConnections, bonds)
        ledgerProtocol.updateBonds(bonds)
    }

    fun updateCharacteristics(characteristics: Set<PropertyEntity>) {
        log.debug(::updateCharacteristics, characteristics)
        ledgerProtocol.updateProps(characteristics)
    }

    private fun onLedgerUpdated(ledger: Ledger) {
        log.debug(::onLedgerUpdated, ledger)
        conflictsSolver.resolve(ledger).forEach(::handle)
    }

    private fun handle(resolution: Resolution) {
        log.debug(::handle, resolution)
        serviceHandlers.getValue(resolution.serviceId).handle(resolution)
    }
}