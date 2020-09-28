package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.Cancellables
import com.fluentbuild.pcas.async.Watcher
import com.fluentbuild.pcas.middleware.ServiceRegistry
import com.fluentbuild.pcas.ledger.models.BondEntity
import com.fluentbuild.pcas.ledger.models.PropertyEntity
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.utils.Mapper

class AudioStateUpdater(
    private val serviceRegistry: ServiceRegistry,
    private val propertyWatcher: Watcher<AudioProperty>,
    private val propertyEntityMapper: Mapper<AudioProperty, Set<PropertyEntity>>,
    private val bondsWatcher: PeripheralBondsWatcher,
    private val bondsEntityMapper: Mapper<Set<PeripheralBond>, Set<BondEntity>>
) {

    fun start(): Cancellable {
        return Cancellables().apply {
            this += propertyWatcher.watch { usage ->
                serviceRegistry.updateProps(propertyEntityMapper.map(usage))
            }

            this += bondsWatcher.watch { connections ->
                serviceRegistry.updateBonds(bondsEntityMapper.map(connections))
            }
        }
    }
}