package com.fluentbuild.pcas.watchers

internal abstract class Watcher<ValueT> {

	private var lastValue: ValueT? = null
	private var onChanged: (() -> Unit)? = null

	fun register(initialValue: ValueT, onChanged: () -> Unit) {
		lastValue = initialValue
		this.onChanged = onChanged
		registerInternal()
	}

	protected fun onUpdated(newValue: ValueT) {
		if(newValue != lastValue) {
			lastValue = newValue
			onChanged?.invoke()
		}
	}

	fun unregister() {
		lastValue = null
		onChanged = null
		unregisterInternal()
	}

	protected abstract fun registerInternal()

	protected abstract fun unregisterInternal()
}