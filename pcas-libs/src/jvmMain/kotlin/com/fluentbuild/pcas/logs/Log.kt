package com.fluentbuild.pcas.logs

import timber.log.*
import kotlin.reflect.KFunction

internal actual class Log actual constructor(private val className: String) {

	private val tree = Timber.tagged(className)
	private val richLog = RichLog

	actual fun debug(function: KFunction<*>, vararg args: Any) {
		val arguments = args.joinToString { it.toString() }
		debug { "${function.name}($arguments)" }
	}

	actual fun debug(message: () -> String) {
		tree.debug(message = message)
	}

	actual fun info(message: () -> String) {
		tree.info(message = message)
		richLog.append(className, message, RichLog.Type.INFO)
	}

	actual fun warn(message: () -> String) {
		tree.warn(message = message)
		richLog.append(className, message, RichLog.Type.WARN)
	}

	actual fun error(message: () -> String) {
		tree.error(message = message)
		richLog.append(className, message, RichLog.Type.ERROR)
	}

	actual fun error(throwable: Throwable, message: () -> String) {
		tree.error(throwable, message)
		richLog.append(className, message, RichLog.Type.ERROR)
		richLog.append(className, { throwable.toString() }, RichLog.Type.ERROR)
	}
}