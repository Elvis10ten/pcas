package com.fluentbuild.pcas.di

import android.os.Handler
import com.fluentbuild.pcas.async.ThreadRunnerAndroid

internal class AsyncModuleAndroid(private val mainThreadHandler: Handler): AsyncModuleJvm() {

    override val threadRunnerProvider = { ThreadRunnerAndroid(mainThreadHandler, threadPool) }
}