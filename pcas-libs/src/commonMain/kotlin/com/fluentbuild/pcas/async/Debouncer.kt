package com.fluentbuild.pcas.async

internal class Debouncer constructor(
	private val runner: ThreadRunner,
	private val delayMillis: Int
) {

	fun debounce(action: () -> Unit) {
		runner.cancelAll()
		runner.runOnMainDelayed(delayMillis, action)
	}

	fun cancel() {
		runner.cancelAll()
	}
}