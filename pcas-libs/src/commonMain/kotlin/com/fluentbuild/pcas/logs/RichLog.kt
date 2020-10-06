package com.fluentbuild.pcas.logs

import com.fluentbuild.pcas.utils.LimitedQueue

object RichLog {

	private const val MAX_CACHE_SIZE = 100
	private val cache = LimitedQueue<String>(MAX_CACHE_SIZE)
	var observer: ((String) -> Unit)? = null

	fun append(tag: String, message: () -> String, type: Type) {
		val text = "<font color='${type.colorHex}'>$ $tag: ${message()}</font><br/><br/>"
		cache.push(text)
		observer?.invoke(text)
	}

	fun getLines() = cache.getElements().joinToString(separator = "")

	enum class Type(val colorHex: String) {
		INFO("#6272a4"),
		WARN("#ffb86c"),
		ERROR("#ff5555")
	}
}