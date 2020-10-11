package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.io.SecureUnicastChannel
import com.fluentbuild.pcas.services.AUDIO_SERVICE_ID
import com.fluentbuild.pcas.stream.StreamDemux
import com.fluentbuild.pcas.stream.StreamHandler

internal class StreamModule(
	unicastChannel: SecureUnicastChannel,
	audioStreamHandler: StreamHandler
) {

	private val serviceStreamHandlers = mapOf(
		AUDIO_SERVICE_ID to audioStreamHandler
	)

	val streamDemux = StreamDemux(
		unicast = unicastChannel,
		handlers = serviceStreamHandlers
	)
}