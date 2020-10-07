package com.fluentbuild.pcas.async

internal class Debouncer(
	private val runner: ThreadRunner
) {

	fun debounce(action: () -> Unit) {
		runner.cancelAll()
		runner.runOnMainDelayed(DELAY, action)
	}

	fun cancel() {
		runner.cancelAll()
	}

	companion object {

		// todo: this should be half the roundtrip time
		private const val DELAY = 100
	}
}