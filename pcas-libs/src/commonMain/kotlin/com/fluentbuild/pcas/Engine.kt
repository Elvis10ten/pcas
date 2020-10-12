package com.fluentbuild.pcas

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.Cancellables
import com.fluentbuild.pcas.ledger.Ledger
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.utils.Delegates.observable

/**
 * The root class to [run] and [stop] PCAS.
 */
class Engine internal constructor(
	private val dependenciesProvider: () -> EngineDependencies
) {

    private val log = getLog()

	val currentEngineState get() = if(currentCancellable == null) State.IDLE else State.RUNNING

	var ledgerCallback: ((Ledger?) -> Unit)? by observable { it?.invoke(currentLedger) }
	var stateCallback: ((State) -> Unit)? by observable { it?.invoke(currentEngineState) }

	private var currentCancellable: Cancellable? by observable { stateCallback?.invoke(currentEngineState) }
	private var currentLedger: Ledger? by observable { ledgerCallback?.invoke(it) }

	fun run() {
		if(currentEngineState == State.RUNNING) return
        log.info { "Running engine!" }

		val dependencies = dependenciesProvider()
		currentCancellable = dependencies.run().apply {
			this += Cancellable {
				log.info { "Stopping engine!" }
				dependencies.release()
				currentLedger = null
				currentCancellable = null
			}
		}
    }

	fun stop() {
		currentCancellable?.cancel()
	}

	private fun EngineDependencies.run(): Cancellables {
		val cancellables = Cancellables()

		try {
			init()
			cancellables += streamDemux.run()
			cancellables += ledgerProtocol.run { ledger ->
				log.info { "Ledger updated" }
				currentLedger = ledger
				contentionsResolver.resolve(ledger)
			}
		} catch (e: Exception) {
			cancellables.cancel()
			release()
			throw e
		}

		return cancellables
	}

	enum class State {
		RUNNING,
		IDLE
	}
}