package com.fluentbuild.pcas.host

import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.services.ServiceClass
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
	var peripherals: MutableMap<ServiceClass, Peripheral> = mutableMapOf(),
	@Transient
	var isAudioCaptureEnabled: Boolean = false
) {

	val requireAudioPeripheral get() = peripherals.getValue(ServiceClass.AUDIO)

	val requireMousePeripheral get() = peripherals.getValue(ServiceClass.MOUSE)

	val requireKeypadPeripheral get() = peripherals.getValue(ServiceClass.KEYPAD)

	val hasAnyPeripheral get() = peripherals.isNotEmpty()
}