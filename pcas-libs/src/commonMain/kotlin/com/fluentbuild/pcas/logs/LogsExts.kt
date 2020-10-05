package com.fluentbuild.pcas.logs

private val CLASS_LOGS = mutableMapOf<String, Log>()

internal fun Any.getLog(): Log {
	val className = this::class.simpleName!!
	return CLASS_LOGS.getOrPut(className) { Log(className) }
}