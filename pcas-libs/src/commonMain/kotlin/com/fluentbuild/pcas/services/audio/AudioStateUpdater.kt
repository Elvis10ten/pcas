package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.Cancellables
import com.fluentbuild.pcas.async.Observable
import com.fluentbuild.pcas.middleware.ServiceRegistry

internal class AudioStateUpdater(
    private val serviceRegistry: ServiceRegistry,
    private val propertyObservable: Observable<AudioProperty>,
    private val bondsObservable: AudioBondsObservable,
    private val blocksBuilder: AudioBlocksBuilder,
) {

    fun start(): Cancellable {
        // todo
        val update = {
            blocksBuilder.buildNew()?.let { serviceRegistry.updateBlocks(it) }
        }

        val cancellables = Cancellables()
        cancellables += propertyObservable.subscribe { audioProp ->
            blocksBuilder.setProp(audioProp)
            update()
        }

        cancellables += bondsObservable.subscribe { bonds ->
            blocksBuilder.setBonds(bonds)
            update()
        }

        return Cancellable {
            cancellables.cancel()
            blocksBuilder.clear()
        }
    }
}