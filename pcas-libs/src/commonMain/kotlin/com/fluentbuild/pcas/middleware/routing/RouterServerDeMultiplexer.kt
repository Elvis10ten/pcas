package com.fluentbuild.pcas.middleware.routing

import com.fluentbuild.pcas.io.PayloadReceiver
import com.fluentbuild.pcas.io.UnicastChannel
import com.fluentbuild.pcas.ledger.models.ServiceId
import com.fluentbuild.pcas.utils.logger
import kotlinx.serialization.protobuf.ProtoBuf

class RouterServerDeMultiplexer(
    private val protoBuf: ProtoBuf,
    private val unicastChannel: UnicastChannel,
    private val serviceRouters: Map<ServiceId, RouterServer>
) : PayloadReceiver {

    private val log by logger()
    private val frameSerializer = RouterFrame.serializer()

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