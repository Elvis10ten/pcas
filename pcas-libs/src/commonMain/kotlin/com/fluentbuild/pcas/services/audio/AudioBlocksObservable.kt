package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.Cancellables
import com.fluentbuild.pcas.values.Observable
import com.fluentbuild.pcas.ledger.Block
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.async.Debouncer

internal class AudioBlocksObservable(
	private val propObservable: Observable<AudioProperty>,
	private val bondsObservable: Observable<PeripheralBond>,
	private val debouncer: Debouncer,
	private val blocksBuilderProvider: () -> AudioBlocksBuilder
): Observable<Set<Block>> {

    override fun subscribe(observer: (Set<Block>) -> Unit): Cancellable {
        val builder = blocksBuilderProvider()
        val cancellables = Cancellables()
        val notifyObserver = {
			debouncer.debounce { builder.buildNovel(observer) }
		}

        cancellables += propObservable.subscribe {
            builder.setProperty(it)
            notifyObserver()
        }

        cancellables += bondsObservable.subscribe {
            builder.setBond(it)
            notifyObserver()
        }

        return Cancellable {
			cancellables.cancel()
			debouncer.cancel()
		}
    }
}