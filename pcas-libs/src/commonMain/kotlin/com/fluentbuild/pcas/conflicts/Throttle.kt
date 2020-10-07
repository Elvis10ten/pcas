package com.fluentbuild.pcas.conflicts

import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.utils.TimeProvider
import com.fluentbuild.pcas.utils.Timestamp

internal class Throttle(
	private val timeProvider: TimeProvider
) {

	private val entries = mutableMapOf<Conflict.Resolution, Timestamp>()

	fun record(resolution: Conflict.Resolution): Boolean {
		val currentTimestamp = timeProvider.currentTimeMillis()
		val entry = entries.getOrPut(resolution) { DEFAULT_TIME }

		val hasSatistifiedInterval = currentTimestamp - entry > INTERVAL_MILLIS

		if(hasSatistifiedInterval) {
			entries[resolution] = currentTimestamp
		}

		return hasSatistifiedInterval
	}

	fun reset() {
		entries.clear()
	}

	companion object {

		private const val INTERVAL_MILLIS = 2000
		private const val DEFAULT_TIME = 0L
	}
}