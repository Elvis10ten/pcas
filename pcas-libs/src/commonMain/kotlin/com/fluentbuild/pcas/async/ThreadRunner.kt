package com.fluentbuild.pcas.async

internal interface ThreadRunner {

    fun runOnMain(action: () -> Unit)

    fun runOnMainDelayed(delayMillis: Int, action: () -> Unit)

    fun runOnMainRepeating(frequencyMillis: Int, action: () -> Unit)

    fun runOnIo(action: () -> Unit)

    fun cancelAll()
}