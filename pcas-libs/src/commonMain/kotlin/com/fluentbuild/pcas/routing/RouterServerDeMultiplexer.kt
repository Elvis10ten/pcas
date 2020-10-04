package com.fluentbuild.pcas.routing

import com.fluentbuild.pcas.io.MessageReceiver
import com.fluentbuild.pcas.io.UnicastChannel
import com.fluentbuild.pcas.services.ServiceId
import com.fluentbuild.pcas.utils.logger
import kotlinx.serialization.protobuf.ProtoBuf

internal class RouterServerDeMultiplexer(
    private val protoBuf: ProtoBuf,
    private val unicastChannel: UnicastChannel,
    private val serviceRouters: Map<ServiceId, RouterServer>
) : MessageReceiver {

    private val log by logger()
    private val frameSerializer = RouterMessage.serializer()

    fun init() {
        log.debug(::init)
        unicastChannel.init(this)
    }

    fun close() {
        log.debug(::close)
        serviceRouters.values.forEach { it.release() }
        unicastChannel.close()
    }

    override fun onReceived(data: ByteArray) {
        val frame = protoBuf.decodeFromByteArray(frameSerializer, data)
        log.info { "Received frame: $frame" }
        serviceRouters.getValue(frame.serviceId).onReceived(frame.sender, frame.payload)
    }
}