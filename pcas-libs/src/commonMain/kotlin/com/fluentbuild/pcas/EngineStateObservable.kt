package com.fluentbuild.pcas

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.host.HostConfig
import com.fluentbuild.pcas.ledger.Ledger
import com.fluentbuild.pcas.utils.Delegates.observable
import com.fluentbuild.pcas.values.Observable

class EngineStateObservable: Observable<EngineState> {

	private val observers = mutableSetOf<Function1<EngineState, Unit>>()
	//TODO
	var currentState by observable(EngineState()) { notifyObservers() }
		private set

	override fun subscribe(observer: (EngineState) -> Unit): Cancellable {
		observers.add(observer)
		observer(currentState)
		return Cancellable { observers.remove(observer) }
	}

	internal fun update(ledger: Ledger?) {
		currentState = currentState.copy(ledger = ledger)
	}

	internal fun update(engineStatus: Engine.Status) {
		currentState = currentState.copy(engineStatus = engineStatus)
	}

	internal fun update(newHostConfig: HostConfig) {
		currentState = currentState.copy().apply {
			hostConfig = newHostConfig
		}
	}

	private fun notifyObservers() {
		observers.forEach { it(currentState) }
	}
}