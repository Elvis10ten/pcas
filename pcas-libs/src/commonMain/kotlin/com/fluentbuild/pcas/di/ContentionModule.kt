package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.contention.ResolutionThrottler
import com.fluentbuild.pcas.contention.ContentionsResolver
import com.fluentbuild.pcas.contention.ResolutionHandler
import com.fluentbuild.pcas.services.ServiceClass
import com.fluentbuild.pcas.utils.TimeProvider

internal class ContentionModule(
	timeProvider: TimeProvider,
	audioResolutionHandler: ResolutionHandler
) {

	private val serviceResolutionHandlers = mapOf(
		ServiceClass.AUDIO to audioResolutionHandler
	)

	private val resolutionThrottler = ResolutionThrottler(timeProvider)

	val contentionsResolver = ContentionsResolver(serviceResolutionHandlers, resolutionThrottler)
}