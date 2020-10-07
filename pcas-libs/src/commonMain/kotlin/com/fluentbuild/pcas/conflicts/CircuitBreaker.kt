package com.fluentbuild.pcas.conflicts

import com.fluentbuild.pcas.utils.TimeProvider

internal class CircuitBreaker(
	private val timeProvider: TimeProvider
) {

	private val entries = mutableMapOf<Conflict.Resolution, AdjacentEntry>()

	fun record(resolution: Conflict.Resolution): Boolean {
		val currentTimestamp = timeProvider.currentTimeMillis()
		val entry = entries.getOrPut(resolution) { AdjacentEntry(0, currentTimestamp) }
		val isAdjacent = currentTimestamp - entry.timestamp < ADJACENT_ENTRY_THRESHOLD_MILLIS

		if(isAdjacent) {
			if(entry.counter >= MAX_ADJACENT_COUNTER) {
				return false
			}

			entry.counter++
		} else {
			entry.counter = 0
		}

		entry.timestamp = currentTimestamp
		return true
	}

	fun reset() {
		entries.clear()
	}

	private data class AdjacentEntry(
		var counter: Int,
		var timestamp: Long
	)

	companion object {

		const val ADJACENT_ENTRY_THRESHOLD_MILLIS = 20 * 1000
		const val MAX_ADJACENT_COUNTER = 3
	}
}