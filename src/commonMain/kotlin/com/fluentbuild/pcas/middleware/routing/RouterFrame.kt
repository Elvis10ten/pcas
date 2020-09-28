package com.fluentbuild.pcas.middleware.routing

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.ledger.models.ServiceId
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
class RouterFrame(
    @ProtoNumber(1)
    val serviceId: ServiceId,
    @ProtoNumber(2)
    val sender: HostInfo,
    @ProtoNumber(3)
    val payload: ByteArray
) {

    override fun toString(): String {
        return """
            |${this::class.simpleName}(
            |${this::serviceId.name}=$serviceId,
            |${this::sender.name}=$sender
            |}
            |""".trimMargin()
    }
}