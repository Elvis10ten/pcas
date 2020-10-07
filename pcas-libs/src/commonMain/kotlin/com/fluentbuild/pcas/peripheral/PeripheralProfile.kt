package com.fluentbuild.pcas.peripheral

import kotlinx.serialization.Serializable

@Serializable
enum class PeripheralProfile(val id: Int) {
	A2DP(1),
	HSP(2),
	HID(3)
}