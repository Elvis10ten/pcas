package com.fluentbuild.pcas.stream

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.io.transport.UnicastChannel
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.services.ServiceClass
import com.fluentbuild.pcas.utils.decode
import kotlinx.serialization.protobuf.ProtoBuf

internal class StreamDemuxer(
	private val unicastChannel: UnicastChannel,
	private val protoBuf: ProtoBuf,
	private val handlers: Map<ServiceClass, StreamHandler>
) {

    private val log = getLog()

    fun run(): Cancellable {
        log.debug { "Running StreamDemux" }

        unicastChannel.init { message, messageSize ->
            val streamMessage = protoBuf.decode<StreamMessage>(message, messageSize)
            handlers.getValue(streamMessage.serviceClass).handle(streamMessage)
        }

        return Cancellable {
            log.debug { "Stopping StreamDemux" }
            handlers.values.forEach { it.release() }
            unicastChannel.close()
        }
    }
}