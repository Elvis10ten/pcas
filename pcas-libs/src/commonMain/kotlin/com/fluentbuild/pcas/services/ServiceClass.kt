package com.fluentbuild.pcas.services

import com.fluentbuild.pcas.host.Uuid
import kotlinx.serialization.Serializable

@Serializable
enum class ServiceClass {

	AUDIO {

		override fun isPeripheralSupported(uuids: Set<Uuid>) =
			uuids.contains(A2DP_UUID) && (uuids.contains(HSP_UUID) || uuids.contains(HFP_UUID))
	},
	KEYPAD {

		override fun isPeripheralSupported(uuids: Set<Uuid>) = uuids.contains(HID_UUID)
	},
	MOUSE {

		override fun isPeripheralSupported(uuids: Set<Uuid>) = uuids.contains(HID_UUID)
	};

	abstract fun isPeripheralSupported(uuids: Set<Uuid>): Boolean
}