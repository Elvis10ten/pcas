package com.fluentbuild.pcas.async

interface ThreadRunner {

    fun runOnMain(action: () -> Unit)

    fun runOnMainRepeating(frequencyMillis: Int, action: () -> Unit)

    fun runOnBackground(action: () -> Unit)

    fun cancelAll()
}