package com.fluentbuild.pcas.services

import com.fluentbuild.pcas.host.Uuid
import kotlinx.serialization.Serializable

@Serializable
enum class ServiceClass(
	val defaultMaxConcurrentConnections: Int
) {

	AUDIO(2) {

		override fun isPeripheralSupported(uuids: Set<Uuid>) =
			uuids.contains(A2DP_UUID) && (uuids.contains(HSP_UUID) || uuids.contains(HFP_UUID))
	},
	KEYPAD(1) {

		override fun isPeripheralSupported(uuids: Set<Uuid>) = uuids.contains(HID_UUID)
	},
	MOUSE(1) {

		override fun isPeripheralSupported(uuids: Set<Uuid>) = uuids.contains(HID_UUID)
	};

	abstract fun isPeripheralSupported(uuids: Set<Uuid>): Boolean
}