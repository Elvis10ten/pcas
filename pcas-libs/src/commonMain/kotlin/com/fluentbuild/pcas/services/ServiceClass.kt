package com.fluentbuild.pcas.services

import kotlinx.serialization.Serializable

@Serializable
enum class ServiceClass(val classId: Int) {
	AUDIO(1),
	KEYPAD(2),
	MOUSE(3)
}