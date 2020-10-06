package com.fluentbuild.pcas.logs

import kotlin.reflect.KFunction

internal class SimpleLog(private val className: String) {

    private val richLog = RichLog

    fun debug(message: () -> String) {
        println("D/$className: ${message()}")
    }

    fun info(message: () -> String) {
        println("I/$className: ${message()}")
        richLog.append(className, message, RichLog.Type.INFO)
    }

    fun warn(message: () -> String) {
        println("W/$className: ${message()}")
        richLog.append(className, message, RichLog.Type.WARN)
    }

    fun error(message: () -> String) {
        println("E/$className: ${message()}")
        richLog.append(className, message, RichLog.Type.ERROR)
    }

    fun error(throwable: Throwable, message: () -> String) {
        println("E/$className: ${message()}")
        throwable.printStackTrace()
        richLog.append(className, message, RichLog.Type.ERROR)
        richLog.append(className, { throwable.toString() }, RichLog.Type.ERROR)
    }

    fun debug(function: KFunction<*>, vararg args: Any) {
        val argsText = args.joinToString { it.toString() }
        debug { "${function.name}($argsText)" }
    }
}