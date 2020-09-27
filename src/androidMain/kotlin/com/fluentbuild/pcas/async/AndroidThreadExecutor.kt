package com.fluentbuild.pcas.async

import android.os.Handler
import androidx.annotation.MainThread
import java.util.concurrent.ThreadPoolExecutor

class AndroidThreadExecutor(
    private val mainThreadHandler: Handler,
    threadPool: ThreadPoolExecutor
): JvmThreadExecutor(threadPool) {

    private val runnables = mutableSetOf<Runnable>()

    override fun onMain(action: () -> Unit) {
        val runnable = object: Runnable {

            override fun run() {
                action()
                runnables.remove(this)
            }
        }

        runnables += runnable
        mainThreadHandler.post(runnable)
    }

    override fun onMainRepeating(interval: Int, action: () -> Unit) {
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
    override fun cancel() {
        super.cancel()
        runnables.forEach { mainThreadHandler.removeCallbacks(it) }
        runnables.clear()
    }
}