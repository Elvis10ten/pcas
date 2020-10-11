package com.fluentbuild.pcas.async

internal class Debouncer(
	private val runner: ThreadRunner
) {

	fun debounce(cancellable: Cancellable, action: () -> Unit): Cancellable {
		cancellable.cancel()
		runner.runOnMainDelayed(DELAY, action)

		return Cancellable {
			runner.cancelAll()
		}
	}

	companion object {

		private const val DELAY = 100
	}
}