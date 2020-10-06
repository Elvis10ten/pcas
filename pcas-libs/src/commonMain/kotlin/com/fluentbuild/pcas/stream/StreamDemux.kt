package com.fluentbuild.pcas.stream

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.io.UnicastChannel
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.services.ServiceId

internal class StreamDemux(
    private val unicast: UnicastChannel,
    private val handlers: Map<ServiceId, StreamHandler>
) {

    private val log = getLog()

    fun run(): Cancellable {
        log.debug { "Running StreamDemux" }

        unicast.init { message, size ->
            handlers.getValue(message.serviceId).handle(
                sender = message.sender,
                payload = message,
                offset = PAYLOAD_OFFSET,
                size = size.payloadSize
            )
        }

        return Cancellable {
            log.debug { "Stopping StreamDemux" }
            handlers.values.forEach { it.release() }
            unicast.close()
        }
    }
}