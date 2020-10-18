package com.fluentbuild.pcas.logs

import kotlin.reflect.KFunction

internal class SimpleLog(private val className: String) {

    fun debug(message: () -> String) {
        println("D/$className: ${message()}")
    }

    fun info(message: () -> String) {
        println("I/$className: ${message()}")
    }

    fun warn(message: () -> String) {
        println("W/$className: ${message()}")
    }

    fun error(message: () -> String) {
        println("E/$className: ${message()}")
    }

    fun error(throwable: Throwable, message: () -> String) {
        println("E/$className: ${message()}")
        throwable.printStackTrace()
    }

    fun debug(function: KFunction<*>, vararg args: Any) {
        debug { "${function.name}(${args.joinToString()})" }
    }
}