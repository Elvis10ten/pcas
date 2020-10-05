package com.fluentbuild.pcas.logs

internal object ConsolePublisher {

	var isEnabled = false
	var observer: ((String) -> Unit) = {}

	fun publish(message: () -> String, type: Type) {
		if(!isEnabled) return
		observer("<font color='${type.colorHex}'>$ ${message()}</font>")
	}

	enum class Type(val colorHex: String) {
		INFO("#6272a4"),
		WARN("#ffb86c"),
		ERROR("#ff5555")
	}
}