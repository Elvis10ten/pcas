package com.fluentbuild.pcas.stream

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.io.SecureUnicastChannel
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.services.ServiceClass

internal class StreamDemux(
    private val unicast: SecureUnicastChannel,
    private val handlers: Map<ServiceClass, StreamHandler>
) {

    private val log = getLog()

    fun run(): Cancellable {
        log.debug { "Running StreamDemux" }

        unicast.init { message, size ->
            handlers.getValue(message.service).handle(
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