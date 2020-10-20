package com.fluentbuild.pcas

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.Cancellables
import com.fluentbuild.pcas.logs.getLog

/**
 * The root class to [run] and [stop] PCAS.
 */
class Engine internal constructor(
	private val stateObservable: EngineStateObservable,
	private val componentProvider: () -> EngineComponent
) {

    private val log = getLog()

	private var currentCancellable: Cancellable? = null

	fun run() {
		if(currentCancellable != null) return
        log.info { "Running engine!" }

		val dependencies = componentProvider()
		currentCancellable = dependencies.run().apply {
			stateObservable.update(Status.RUNNING)
			this += Cancellable {
				log.info { "Stopping engine!" }
				dependencies.release()
				stateObservable.update(null)
				stateObservable.update(Status.IDLE)
				currentCancellable = null
			}
		}
    }

	fun stop() {
		currentCancellable?.cancel()
		currentCancellable = null
	}

	private fun EngineComponent.run(): Cancellables {
		val cancellables = Cancellables()

		try {
			cancellables += streamDemuxer.run()
			cancellables += ledgerProtocol.run { ledger ->
				log.info { "Ledger updated" }
				stateObservable.update(ledger)
				contentionsResolver.resolve(ledger)
			}
		} catch (e: Exception) {
			cancellables.cancel()
			release()
			throw e
		}

		return cancellables
	}

	enum class Status {
		RUNNING,
		IDLE
	}
}