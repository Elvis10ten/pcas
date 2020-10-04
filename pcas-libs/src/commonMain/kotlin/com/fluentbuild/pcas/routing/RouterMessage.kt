package com.fluentbuild.pcas.routing

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.services.ServiceId
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
class RouterMessage(
	@ProtoNumber(1)
    val serviceId: ServiceId,
	@ProtoNumber(2)
    val sender: HostInfo,
	@ProtoNumber(3)
    val payload: ByteArray
)