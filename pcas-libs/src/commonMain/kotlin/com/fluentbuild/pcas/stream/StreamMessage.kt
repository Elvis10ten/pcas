package com.fluentbuild.pcas.stream

import com.fluentbuild.pcas.host.Uuid
import com.fluentbuild.pcas.services.ServiceClass
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
class StreamMessage(
	@ProtoNumber(1)
	val serviceClass: ServiceClass,
	@ProtoNumber(2)
	val sender: Uuid,
	@ProtoNumber(3)
	val payload: ByteArray
)