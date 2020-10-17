package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.io.UnicastChannel
import com.fluentbuild.pcas.services.ServiceClass
import com.fluentbuild.pcas.stream.StreamDemuxer
import com.fluentbuild.pcas.stream.StreamHandler
import com.fluentbuild.pcas.stream.StreamMux
import kotlinx.serialization.protobuf.ProtoBuf

internal class StreamModule(
	unicastChannel: UnicastChannel,
	audioStreamHandler: StreamHandler,
	protoBuf: ProtoBuf
) {

	private val serviceStreamHandlers = mapOf(
		ServiceClass.AUDIO to audioStreamHandler
	)

	val streamDemuxer = StreamDemuxer(
		unicastChannel = unicastChannel,
		handlers = serviceStreamHandlers,
		protoBuf = protoBuf
	)

	val streamMuxer = StreamMux(
		unicastChannel = unicastChannel,
		protoBuf = protoBuf
	)
}