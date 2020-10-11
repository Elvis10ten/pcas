package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.contention.ResolutionThrottler
import com.fluentbuild.pcas.contention.ContentionsResolver
import com.fluentbuild.pcas.contention.ResolutionHandler
import com.fluentbuild.pcas.services.AUDIO_SERVICE_ID
import com.fluentbuild.pcas.utils.JvmTimeProvider

internal class ConflictsModule(
	timeProvider: JvmTimeProvider,
	audioResolutionHandler: ResolutionHandler
) {

	private val serviceHandlers = mapOf(
		AUDIO_SERVICE_ID to audioResolutionHandler
	)

	private val circuitBreaker = ResolutionThrottler(timeProvider)

	val conflictsResolver = ContentionsResolver(serviceHandlers, circuitBreaker)
}