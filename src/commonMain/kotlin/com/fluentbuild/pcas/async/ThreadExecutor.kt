package com.fluentbuild.pcas.async

interface ThreadExecutor {

    fun onMain(action: () -> Unit)

    fun onMainRepeating(interval: Int, action: () -> Unit)

    fun onBackground(action: () -> Unit)

    fun cancel()
}