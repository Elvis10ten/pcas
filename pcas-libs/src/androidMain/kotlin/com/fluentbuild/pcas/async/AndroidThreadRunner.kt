package com.fluentbuild.pcas.async

import android.os.Handler
import androidx.annotation.MainThread
import java.util.concurrent.ThreadPoolExecutor

class AndroidThreadRunner(
    private val mainThreadHandler: Handler,
    threadPool: ThreadPoolExecutor
): JvmThreadRunner(threadPool) {

    private val runnables = mutableSetOf<Runnable>()

    override fun runOnMain(action: () -> Unit) {
        val runnable = object: Runnable {

            override fun run() {
                action()
                runnables.remove(this)
            }
        }

        runnables += runnable
        mainThreadHandler.post(runnable)
    }

    override fun runOnMainRepeating(interval: Int, action: () -> Unit) {
        val runnable = object: Runnable {

            override fun run() {
                action()
                if(runnables.contains(this)) {
                    mainThreadHandler.postDelayed(this, interval.toLong())
                }
            }
        }

        runnables += runnable
        mainThreadHandler.postDelayed(runnable, interval.toLong())
    }

    @MainThread
    override fun cancelAll() {
        super.cancelAll()
        runnables.forEach { mainThreadHandler.removeCallbacks(it) }
        runnables.clear()
    }
}