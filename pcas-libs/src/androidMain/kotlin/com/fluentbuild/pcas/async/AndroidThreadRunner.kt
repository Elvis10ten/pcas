package com.fluentbuild.pcas.async

import android.os.Handler
import androidx.annotation.MainThread
import java.util.concurrent.ThreadPoolExecutor

internal class AndroidThreadRunner(
    private val mainThreadHandler: Handler,
    threadPool: ThreadPoolExecutor
): JvmThreadRunner(threadPool) {

    private val mainThreadRunnables = mutableSetOf<Runnable>()

    override fun runOnMain(action: () -> Unit) {
        val runnable = object: Runnable {

            override fun run() {
                action()
                mainThreadRunnables.remove(this)
            }
        }

        mainThreadRunnables += runnable
        mainThreadHandler.post(runnable)
    }

    override fun runOnMainRepeating(interval: Int, action: () -> Unit) {
        val intervalLong = interval.toLong()
        val runnable = object: Runnable {

            override fun run() {
                action()
                if(mainThreadRunnables.contains(this)) {
                    mainThreadHandler.postDelayed(this, intervalLong)
                }
            }
        }

        mainThreadRunnables += runnable
        mainThreadHandler.postDelayed(runnable, intervalLong)
    }

    @MainThread
    override fun cancelAll() {
        super.cancelAll()
        mainThreadRunnables.forEach { mainThreadHandler.removeCallbacks(it) }
        mainThreadRunnables.clear()
    }
}