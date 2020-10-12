package com.fluentbuild.pcas.services

import kotlinx.serialization.Serializable

typealias ServiceClassId = Int

@Serializable
enum class ServiceClass(val classId: ServiceClassId) {
	AUDIO(1),
	KEYPAD(2),
	MOUSE(3)
}