package com.fluentbuild.pcas.async

interface ThreadExecutor {

    fun onMain(action: () -> Unit): Cancellable

    fun onBackground(action: () -> Unit): Cancellable

    fun cancel()
}