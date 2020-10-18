package com.fluentbuild.pcas.contention

import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.utils.ElapsedRealtime
import com.fluentbuild.pcas.utils.TimeProvider

/**
 * Throttles resolutions so handlers are only called at most once per [MIN_INTERVAL_MILLIS]
 */
internal class ResolutionThrottler(private val timeProvider: TimeProvider) {

	private val log = getLog()
	private val lastResolutionsTimes = mutableMapOf<Contention.Resolution, ElapsedRealtime>()

	fun throttle(resolution: Contention.Resolution, action: (Contention.Resolution) -> Unit) {
		log.debug(::throttle, resolution)
		val currentElapsedRealtime = timeProvider.elapsedRealtime
		val lastTimeForResolution = lastResolutionsTimes[resolution]

		val hasSatisfiedInterval = lastTimeForResolution == null ||
				currentElapsedRealtime - lastTimeForResolution > MIN_INTERVAL_MILLIS

		if(hasSatisfiedInterval) {
			log.info { "Handling resolution: ${resolution::class.simpleName} for: ${resolution.selfBlock.profile}" }
			lastResolutionsTimes[resolution] = currentElapsedRealtime
			action(resolution)
		} else {
			log.warn { "Throttling resolution: ${resolution::class.simpleName}" }
		}
	}

	fun reset() {
		lastResolutionsTimes.clear()
	}

	companion object {

		private const val MIN_INTERVAL_MILLIS = 2000
	}
}