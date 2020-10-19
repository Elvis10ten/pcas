package com.fluentbuild.pcas.host

import com.fluentbuild.pcas.peripheral.Peripheral
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
class HostConfig(
	@ProtoNumber(1)
	val uuid: Uuid,
	@ProtoNumber(2)
	var name: String,
	@ProtoNumber(3)
	var networkKey: ByteArray? = null,
	@ProtoNumber(4)
	var audioPeripheral: Peripheral? = null
) {

	@Transient
	var canCaptureAudio: Boolean = false
}