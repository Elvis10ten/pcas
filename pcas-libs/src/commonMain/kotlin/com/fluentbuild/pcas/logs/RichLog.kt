package com.fluentbuild.pcas.logs

import com.fluentbuild.pcas.AppStateObservable

object RichLog {

	var appStateObservable: AppStateObservable? = null

	fun append(tag: String, message: () -> String, type: Type) {
		val line = "<font color='${type.colorHex}'>$ $tag: ${message()}</font><br/><br/>"
		appStateObservable?.update(line)
	}

	enum class Type(val colorHex: String) {
		INFO("#6272a4"),
		WARN("#ffb86c"),
		ERROR("#ff5555")
	}
}