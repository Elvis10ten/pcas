package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.async.Debouncer
import com.fluentbuild.pcas.conflicts.Throttle
import com.fluentbuild.pcas.conflicts.ConflictsResolver
import com.fluentbuild.pcas.conflicts.ResolutionHandler
import com.fluentbuild.pcas.services.AUDIO_SERVICE_ID
import com.fluentbuild.pcas.utils.TimeProvider

internal class ConflictsModule(
	timeProvider: TimeProvider,
	audioResolutionHandler: ResolutionHandler,
	debouncer: () -> Debouncer
) {

	private val serviceHandlers = mapOf(
		AUDIO_SERVICE_ID to audioResolutionHandler
	)

	private val circuitBreaker = Throttle(timeProvider)

	val conflictsResolver = ConflictsResolver(serviceHandlers, circuitBreaker, debouncer())
}