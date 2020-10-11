package com.fluentbuild.pcas

import com.fluentbuild.pcas.peripheral.Peripheral

class HostConfig(
	val uuid: Uuid,
	val name: String,
	val networkKey: ByteArray,
	val audioPeripheral: Peripheral,
	val canCaptureAudio: Boolean
)