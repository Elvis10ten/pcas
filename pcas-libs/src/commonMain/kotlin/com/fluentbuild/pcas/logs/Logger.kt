package com.fluentbuild.pcas.logs

import kotlin.reflect.KFunction

class Logger(className: String) {

    private val client = getLogClient(className)
    private val publisher = ConsolePublisher

    fun debug(function: KFunction<*>, vararg args: Any) {
        client.debug(function, args)
    }

    fun debug(message: () -> String) {
        client.debug(message)
    }

    fun info(message: () -> String) {
        client.info(message)
        publisher.publish(message, ConsolePublisher.Type.INFO)
    }

    fun warn(message: () -> String) {
        client.warn(message)
        publisher.publish(message, ConsolePublisher.Type.WARN)
    }

    fun error(message: () -> String) {
        client.error(message)
        publisher.publish(message, ConsolePublisher.Type.ERROR)
    }

    fun error(throwable: Throwable, message: () -> String) {
        client.error(throwable, message)
        publisher.publish(message, ConsolePublisher.Type.ERROR)
        publisher.publish({ throwable.toString() }, ConsolePublisher.Type.ERROR)
    }
}