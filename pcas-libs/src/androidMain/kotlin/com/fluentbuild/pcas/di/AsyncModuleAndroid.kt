package com.fluentbuild.pcas.di

import android.os.Handler
import com.fluentbuild.pcas.async.AndroidThreadRunner

internal class AsyncModuleAndroid(private val mainThreadHandler: Handler): AsyncModuleJvm() {

    override val threadRunnerProvider = { AndroidThreadRunner(mainThreadHandler, threadPool) }
}