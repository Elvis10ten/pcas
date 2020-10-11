package com.fluentbuild.pcas.async

import com.fluentbuild.pcas.logs.getLog
import java.util.concurrent.Future
import java.util.concurrent.ThreadPoolExecutor

internal open class JvmThreadRunner(
    private val threadPool: ThreadPoolExecutor
): ThreadRunner {

    private val log = getLog()
    private val futures = mutableSetOf<Future<*>>()

    override fun runOnMain(action: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun runOnMainDelayed(delayMillis: Int, action: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun runOnMainRepeating(frequencyMillis: Int, action: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun runOnIo(action: () -> Unit) {
        futures += threadPool.submit {
            try {
                requireNotInterrupted()
                action()
            } catch (e: Exception) {
                log.error(e) { "Error running on IO" }
            }
        }
    }

    override fun cancelAll() {
        futures.forEach { it.cancel(true) }
        futures.clear()
    }
}