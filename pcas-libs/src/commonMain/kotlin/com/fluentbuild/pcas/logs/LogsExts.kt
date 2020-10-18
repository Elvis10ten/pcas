package com.fluentbuild.pcas.logs

private val CLASS_LOGS = mutableMapOf<String, Log>()

internal fun Any.getLog() = getLog(this::class.simpleName!!)

internal fun getLog(className: String) = CLASS_LOGS.getOrPut(className) { Log(className) }