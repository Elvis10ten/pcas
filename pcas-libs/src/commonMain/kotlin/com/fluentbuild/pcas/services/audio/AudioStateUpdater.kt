package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.async.Cancellables
import com.fluentbuild.pcas.async.Observable
import com.fluentbuild.pcas.middleware.ServiceRegistry
import com.fluentbuild.pcas.ledger.models.BondEntity
import com.fluentbuild.pcas.ledger.models.PropertyEntity
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.services.Mapper

class AudioStateUpdater internal constructor(
    private val serviceRegistry: ServiceRegistry,
    private val propertyObservable: Observable<AudioProperty>,
    private val propertyEntityMapper: Mapper<AudioProperty, Set<PropertyEntity>>,
    private val bondsObservable: PeripheralBondsObservable,
    private val bondsEntityMapper: Mapper<Set<PeripheralBond>, Set<BondEntity>>
) {

    private val cancellables = Cancellables()

    fun start() {
        cancellables += propertyObservable.subscribe { usage ->
            serviceRegistry.updateProps(propertyEntityMapper.map(usage))
        }
        cancellables += bondsObservable.subscribe { connections ->
            serviceRegistry.updateBonds(bondsEntityMapper.map(connections))
        }
    }

    fun stop() {
        cancellables.cancel()
    }
}