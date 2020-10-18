package com.fluentbuild.pcas

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.host.HostConfig
import com.fluentbuild.pcas.ledger.Ledger
import com.fluentbuild.pcas.utils.Delegates.observable
import com.fluentbuild.pcas.values.Observable

class EngineStateObservable: Observable<EngineState> {

	private val observers = mutableSetOf<Function1<EngineState, Unit>>()
	//TODO
	var currentAppState by observable(EngineState()) { notifyObservers() }
		private set

	override fun subscribe(observer: (EngineState) -> Unit): Cancellable {
		observers.add(observer)
		observer(currentAppState)
		return Cancellable { observers.remove(observer) }
	}

	internal fun update(ledger: Ledger?) {
		currentAppState = currentAppState.copy(ledger = ledger)
	}

	internal fun update(engineStatus: Engine.Status) {
		currentAppState = currentAppState.copy(engineStatus = engineStatus)
	}

	internal fun update(hostConfig: HostConfig) {
		currentAppState = currentAppState.copy(hostConfig = hostConfig)
	}

	private fun notifyObservers() {
		observers.forEach { it(currentAppState) }
	}
}