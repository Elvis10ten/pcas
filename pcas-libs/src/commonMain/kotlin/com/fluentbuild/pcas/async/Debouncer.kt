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

		private const val DELAY = 100
	}
}