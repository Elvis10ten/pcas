package com.fluentbuild.pcas

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.Cancellables
import com.fluentbuild.pcas.logs.getLog

/**
 * The root class to [run] and [stop] PCAS.
 */
class Engine internal constructor(
	private val appStateObservable: AppStateObservable,
	private val componentProvider: () -> EngineComponent
) {

    private val log = getLog()

	private var currentCancellable: Cancellable? = null

	fun run() {
		if(currentCancellable != null) return
        log.info { "Running engine!" }

		val dependencies = componentProvider()
		currentCancellable = dependencies.run().apply {
			appStateObservable.update(Status.RUNNING)
			this += Cancellable {
				log.info { "Stopping engine!" }
				dependencies.release()
				appStateObservable.update(null)
				appStateObservable.update(Status.IDLE)
				currentCancellable = null
			}
		}
    }

	fun stop() {
		currentCancellable?.cancel()
	}

	private fun EngineComponent.run(): Cancellables {
		val cancellables = Cancellables()

		try {
			cancellables += streamDemuxer.run()
			cancellables += ledgerProtocol.run { ledger ->
				log.info { "Ledger updated" }
				appStateObservable.update(ledger)
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