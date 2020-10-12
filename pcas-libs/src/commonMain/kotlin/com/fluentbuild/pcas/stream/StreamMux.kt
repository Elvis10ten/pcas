package com.fluentbuild.pcas.stream

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.io.SecureUnicastChannel
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

internal class StreamMux(
	private val unicastChannel: SecureUnicastChannel,
	private val protoBuf: ProtoBuf
) {

	fun send(destination: HostInfo, streamMessage: StreamMessage) {
		val message = protoBuf.encodeToByteArray(streamMessage)
		unicastChannel.send(destination, message, message.size)
	}
}