package com.fluentbuild.pcas.logs

import timber.log.*
import kotlin.reflect.KFunction

internal actual class Log actual constructor(private val className: String) {

	private val tree = Timber.tagged(className)

	actual fun debug(function: KFunction<*>, vararg args: Any) {
		val arguments = args.joinToString { it.toString() }
		debug { "${function.name}($arguments)" }
	}

	actual fun debug(message: () -> String) {
		tree.debug(message = message)
	}

	actual fun info(message: () -> String) {
		tree.info(message = message)
	}

	actual fun warn(message: () -> String) {
		tree.warn(message = message)
	}

	actual fun error(message: () -> String) {
		tree.error(message = message)
	}

	actual fun error(throwable: Throwable, message: () -> String) {
		tree.error(throwable, message)
	}
}