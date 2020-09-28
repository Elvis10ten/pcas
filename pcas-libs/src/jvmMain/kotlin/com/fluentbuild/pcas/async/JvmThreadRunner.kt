package com.fluentbuild.pcas.async

import java.util.concurrent.Future
import java.util.concurrent.ThreadPoolExecutor

open class JvmThreadRunner(
    private val threadPool: ThreadPoolExecutor
): ThreadRunner {

    private val futures = mutableSetOf<Future<*>>()

    override fun runOnMain(action: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun runOnMainRepeating(interval: Int, action: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun runOnBackground(action: () -> Unit) {
        futures += threadPool.submit {
            try {
                action()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun cancelAll() {
        futures.forEach { it.cancel(true) }
        futures.clear()
    }

}