package com.fluentbuild.pcas.utils

import timber.log.*
import kotlin.reflect.KFunction

@Suppress("unused")
open class JvmLogger(className: String) {

    private val tree = Timber.tagged(className)

    fun debug(function: KFunction<*>, vararg args: Any) {
        val arguments = args.joinToString { it.toString() }
        debug { "${function.name}($arguments)" }
    }
    
    fun debug(message: () -> String) {
        tree.debug(message = message)
    }

    fun info(message: () -> String) {
        tree.info(message = message)
    }

    fun warn(message: () -> String) {
        tree.warn(message = message)
    }

    fun error(message: () -> String) {
        tree.error(message = message)
    }

    fun error(throwable: Throwable, message: () -> String) {
        tree.error(throwable, message)
    }
}