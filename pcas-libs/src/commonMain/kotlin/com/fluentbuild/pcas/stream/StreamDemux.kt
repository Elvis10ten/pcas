package com.fluentbuild.pcas.stream

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.io.SecureUnicastChannel
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.services.ServiceClassId

internal class StreamDemux(
    private val unicast: SecureUnicastChannel,
    private val handlers: Map<ServiceClassId, StreamHandler>
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