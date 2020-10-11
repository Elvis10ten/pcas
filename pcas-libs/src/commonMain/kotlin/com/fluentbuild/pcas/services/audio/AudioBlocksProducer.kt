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
import com.fluentbuild.pcas.utils.TimeProvider

internal class AudioBlocksProducer(
	private val audioPeripheral: Peripheral,
	private val propObservable: Observable<AudioProperty>,
	private val bondsObservable: Observable<PeripheralBond>,
	private val timeProvider: TimeProvider,
	private val hostObservable: HostInfoObservable,
	private val debouncer: Debouncer,
	private val canPlatformStream: Boolean
): BlocksProducer {

    override fun subscribe(consumer: (Set<Block>) -> Unit): Cancellable {
        val builder = AudioBlocksBuilder(audioPeripheral, timeProvider, hostObservable, canPlatformStream)
        val cancellables = Cancellables()
        val updateConsumer = {
            debouncer.debounce { builder.buildNovel()?.let(consumer) }
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
			debouncer.cancel()
			cancellables.cancel()
		}
    }
}