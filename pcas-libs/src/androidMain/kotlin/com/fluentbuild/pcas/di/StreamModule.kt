package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.io.UnicastChannel
import com.fluentbuild.pcas.services.AUDIO_SERVICE_ID
import com.fluentbuild.pcas.services.ServiceId
import com.fluentbuild.pcas.stream.StreamDemux
import com.fluentbuild.pcas.stream.StreamHandler

internal class StreamModule(
	unicast: UnicastChannel,
	audioStreamHandler: StreamHandler
) {

	private val streamHandlers = mapOf(
		AUDIO_SERVICE_ID to audioStreamHandler
	)

	val streamDemux = StreamDemux(
		unicast = unicast,
		handlers = streamHandlers
	)
}