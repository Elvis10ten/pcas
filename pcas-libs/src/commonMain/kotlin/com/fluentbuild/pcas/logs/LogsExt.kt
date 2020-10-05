package com.fluentbuild.pcas.logs

internal const val MAX_CONSOLE_MESSAGE_SIZE = 100

internal fun Any.logger() = lazy { Logger(this::class.simpleName!!) }

private val logClients = mutableMapOf<String, LogClient>()

internal fun getLogClient(className: String) = logClients.getOrPut(className) { LogClient(className) }