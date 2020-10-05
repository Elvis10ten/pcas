package com.fluentbuild.pcas.stream

import com.fluentbuild.pcas.io.MessageReceiver
import com.fluentbuild.pcas.io.UnicastChannel
import com.fluentbuild.pcas.services.ServiceId
import com.fluentbuild.pcas.logs.getLog

internal class StreamDemux(
    private val unicast: UnicastChannel,
    private val serviceHandlers: Map<ServiceId, StreamHandler>
) : MessageReceiver {

    private val log = getLog()

    fun init() {
        log.debug(::init)
        unicast.init(this)
    }

    fun close() {
        log.debug(::close)
        serviceHandlers.values.forEach { it.release() }
        unicast.close()
    }

    override fun onReceived(marshalledMessage: ByteArray, actualSize: Int) {
        // TODO cleanup this class
        val serviceId = marshalledMessage[INDEX_SERVICE_ID].toInt()
        val senderUuid = "sup" // todo
        log.info { "Service($serviceId) stream message received from: $senderUuid" }
        serviceHandlers.getValue(serviceId).handle(senderUuid, marshalledMessage, DATA_OFFSET, actualSize - DATA_OFFSET)
    }
}