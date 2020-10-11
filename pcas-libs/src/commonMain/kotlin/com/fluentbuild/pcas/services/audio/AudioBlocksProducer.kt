package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.Cancellables
import com.fluentbuild.pcas.values.Observable
import com.fluentbuild.pcas.HostInfoObservable
import com.fluentbuild.pcas.ledger.Block
import com.fluentbuild.pcas.ledger.BlocksProducer
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.async.Debouncer
import com.fluentbuild.pcas.async.SentinelCancellable
import com.fluentbuild.pcas.utils.TimeProvider

internal class AudioBlocksProducer(
	private val propObservable: Observable<AudioProperty>,
	private val bondsObservable: Observable<PeripheralBond>,
	private val debouncer: Debouncer,
	private val audioBlocksBuilderProvider: () -> AudioBlocksBuilder
): BlocksProducer {

    override fun subscribe(consumer: (Set<Block>) -> Unit): Cancellable {
        val builder = audioBlocksBuilderProvider()
        val cancellables = Cancellables()
		var debouncerCancellable: Cancellable = SentinelCancellable
        val updateConsumer = {
			debouncerCancellable = debouncer.debounce(debouncerCancellable) { builder.buildNovel()?.let(consumer) }
        }

        cancellables += propObservable.subscribe {
            builder.setProperty(it)
            updateConsumer()
        }

        cancellables += bondsObservable.subscribe {
            builder.setBond(it)
            updateConsumer()
        }

        return Cancellable {
			debouncerCancellable.cancel()
			cancellables.cancel()
		}
    }
}