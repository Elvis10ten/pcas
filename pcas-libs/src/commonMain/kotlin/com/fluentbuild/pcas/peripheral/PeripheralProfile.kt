package com.fluentbuild.pcas.peripheral

import kotlinx.serialization.Serializable

@Serializable
enum class PeripheralProfile(val supportsStreaming: Boolean) {
	A2DP(true),
	HEADSET(false),
	HID(false)
}