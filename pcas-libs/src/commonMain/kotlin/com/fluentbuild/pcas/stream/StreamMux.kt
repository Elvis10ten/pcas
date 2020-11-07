package com.fluentbuild.pcas.stream

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.io.transport.UnicastChannel
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

internal class StreamMux(
	private val unicastChannel: UnicastChannel,
	private val protoBuf: ProtoBuf
) {

	fun send(destination: HostInfo, streamMessage: StreamMessage) {
		val message = protoBuf.encodeToByteArray(streamMessage)
		unicastChannel.send(destination, message, message.size)
	}
}