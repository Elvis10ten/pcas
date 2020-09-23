package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.Cancellables
import com.fluentbuild.pcas.async.Watcher
import com.fluentbuild.pcas.host.audio.AudioProperty
import com.fluentbuild.pcas.middleware.ServiceRegistry
import com.fluentbuild.pcas.host.PeripheralBondsWatcher

class AudioStateUpdater(
    private val serviceRegistry: ServiceRegistry,
    private val propertyWatcher: Watcher<AudioProperty>,
    private val propertyEntityMapper: PropertyEntityMapper,
    private val bondsWatcher: PeripheralBondsWatcher,
    private val bondsEntityMapper: BondsEntityMapper,
) {

    fun start(): Cancellable {
        return Cancellables().apply {
            this += propertyWatcher.watch { usage ->
                serviceRegistry.updateCharacteristics(propertyEntityMapper.map(usage))
            }

            this += bondsWatcher.watch { connections ->
                serviceRegistry.updateConnections(bondsEntityMapper.map(connections))
            }
        }
    }
}