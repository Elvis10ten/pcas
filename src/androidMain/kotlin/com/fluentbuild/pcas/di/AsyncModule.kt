package com.fluentbuild.pcas.di

import android.os.Handler
import com.fluentbuild.pcas.async.AndroidThreadRunner
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class AsyncModule(
    private val mainThreadHandler: Handler
) {

    val threadPool: ThreadPoolExecutor by lazy {
        ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME_SECONDS,
            TimeUnit.SECONDS,
            LinkedBlockingQueue()
        )
    }

    fun provideThreadExecutor() = AndroidThreadRunner(mainThreadHandler, threadPool)

    companion object {

        // Gets the number of available cores (not always the same as the maximum number of cores)
        private val NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors()

        // The amount of time an idle thread waits before terminating
        private const val KEEP_ALIVE_TIME_SECONDS = 1L
        private val CORE_POOL_SIZE = NUMBER_OF_CORES
        private val MAX_POOL_SIZE = NUMBER_OF_CORES
    }
}