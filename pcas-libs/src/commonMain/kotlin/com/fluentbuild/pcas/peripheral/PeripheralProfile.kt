package com.fluentbuild.pcas.peripheral

import kotlinx.serialization.Serializable

@Serializable
enum class PeripheralProfile(val id: Int, val canStream: Boolean) {
	A2DP(1, true),
	HSP(2, false),
	HID(3, false)
}