package com.fluentbuild.pcas.async

import android.os.Handler
import androidx.annotation.MainThread
import java.util.concurrent.ThreadPoolExecutor

internal class ThreadRunnerAndroid(
    private val mainThreadHandler: Handler,
    threadPool: ThreadPoolExecutor
): ThreadRunnerJvm(threadPool) {

    private val mainThreadRunnables = mutableSetOf<Runnable>()

    override fun runOnMain(action: () -> Unit) {
        createOneOffRunnable(action).apply {
            mainThreadRunnables += this
            mainThreadHandler.post(this)
        }
    }

    override fun runOnMainDelayed(delayMillis: Int, action: () -> Unit) {
        createOneOffRunnable(action).apply {
            mainThreadRunnables += this
            mainThreadHandler.postDelayed(this, delayMillis.toLong())
        }
    }

    override fun runOnMainRepeating(frequencyMillis: Int, action: () -> Unit) {
        val intervalLong = frequencyMillis.toLong()
        val runnable = object: Runnable {

            override fun run() {
                if(canRunOnMainThread()) {
                    action()
                    mainThreadHandler.postDelayed(this, intervalLong)
                }
            }
        }

        mainThreadRunnables += runnable
        mainThreadHandler.postDelayed(runnable, intervalLong)
    }

    private fun Runnable.canRunOnMainThread() = mainThreadRunnables.contains(this)

    private fun createOneOffRunnable(action: () -> Unit): Runnable {
        return object: Runnable {

            override fun run() {
                if(canRunOnMainThread()) {
                    action()
                    mainThreadRunnables.remove(this)
                }
            }
        }
    }

    @MainThread
    override fun cancelAll() {
        super.cancelAll()
        mainThreadRunnables.forEach { mainThreadHandler.removeCallbacks(it) }
        mainThreadRunnables.clear()
    }
}