package com.fluentbuild.pcas.async

import java.util.concurrent.Future
import java.util.concurrent.ThreadPoolExecutor

open class JvmThreadExecutor(
    private val threadPool: ThreadPoolExecutor
): ThreadExecutor {

    private val futures = mutableSetOf<Future<*>>()

    override fun onMain(action: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun onMainRepeating(interval: Int, action: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun onBackground(action: () -> Unit) {
        futures += threadPool.submit {
            try {
                action()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun cancel() {
        futures.forEach { it.cancel(true) }
        futures.clear()
    }

}