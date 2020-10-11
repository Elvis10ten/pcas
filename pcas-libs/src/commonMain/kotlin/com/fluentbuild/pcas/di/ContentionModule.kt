package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.contention.ResolutionThrottler
import com.fluentbuild.pcas.contention.ContentionsResolver
import com.fluentbuild.pcas.contention.ResolutionHandler
import com.fluentbuild.pcas.services.AUDIO_SERVICE_ID
import com.fluentbuild.pcas.utils.TimeProvider

internal class ContentionModule(
	timeProvider: TimeProvider,
	audioResolutionHandler: ResolutionHandler
) {

	private val serviceResolutionHandlers = mapOf(
		AUDIO_SERVICE_ID to audioResolutionHandler
	)

	private val resolutionThrottle = ResolutionThrottler(timeProvider)

	val contentionsResolver = ContentionsResolver(serviceResolutionHandlers, resolutionThrottle)
}