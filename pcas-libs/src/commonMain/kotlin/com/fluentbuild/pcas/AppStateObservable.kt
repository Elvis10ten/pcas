package com.fluentbuild.pcas

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.ledger.Ledger
import com.fluentbuild.pcas.logs.RichLog
import com.fluentbuild.pcas.utils.Delegates.observable
import com.fluentbuild.pcas.utils.LimitedQueue
import com.fluentbuild.pcas.values.Observable

class AppStateObservable: Observable<AppState> {

	private val observers = mutableSetOf<Function1<AppState, Unit>>()
	private val richLogLines = LimitedQueue<String>(MAX_RICH_LOG_SIZE)
	var currentAppState by observable(AppState()) { notifyObservers() }
		private set

	init {
		RichLog.appStateObservable = this
	}

	override fun subscribe(observer: (AppState) -> Unit): Cancellable {
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

	internal fun update(line: String) {
		richLogLines.push(line)
		currentAppState = currentAppState.copy(richLogLines = richLogLines.getElements())
	}

	fun clearLog() {
		richLogLines.clear()
		currentAppState = currentAppState.copy(richLogLines = richLogLines.getElements())
	}

	private fun notifyObservers() {
		observers.forEach { it(currentAppState) }
	}

	companion object {

		private const val MAX_RICH_LOG_SIZE = 100
	}
}