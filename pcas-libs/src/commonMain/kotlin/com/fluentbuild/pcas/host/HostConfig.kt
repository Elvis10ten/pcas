package com.fluentbuild.pcas.host

import com.fluentbuild.pcas.peripheral.Peripheral
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
class HostConfig(
	@ProtoNumber(1)
	val uuid: Uuid,
	@ProtoNumber(2)
	val name: String,
	@ProtoNumber(3)
	val networkKey: ByteArray,
	@ProtoNumber(4)
	val audioPeripheral: Peripheral,
	@ProtoNumber(5)
	val canCaptureAudio: Boolean
)