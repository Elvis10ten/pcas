package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.conflicts.CircuitBreaker
import com.fluentbuild.pcas.conflicts.ConflictsResolver
import com.fluentbuild.pcas.conflicts.ResolutionHandler
import com.fluentbuild.pcas.services.AUDIO_SERVICE_ID
import com.fluentbuild.pcas.utils.TimeProvider

internal class ConflictsModule(
	timeProvider: TimeProvider,
	audioResolutionHandler: ResolutionHandler
) {

	private val serviceHandlers = mapOf(
		AUDIO_SERVICE_ID to audioResolutionHandler
	)

	private val circuitBreaker = CircuitBreaker(timeProvider)

	val conflictsResolver = ConflictsResolver(serviceHandlers, circuitBreaker)
}