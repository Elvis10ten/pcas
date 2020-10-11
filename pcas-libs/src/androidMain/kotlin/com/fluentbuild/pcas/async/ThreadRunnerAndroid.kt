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
        val runnable = object: Runnable {

            override fun run() {
                if(canRunOnMainThread()) {
                    action()
                    mainThreadRunnables.remove(this)
                }
            }
        }

        mainThreadRunnables += runnable
        mainThreadHandler.post(runnable)
    }

    override fun runOnMainDelayed(delayMillis: Int, action: () -> Unit) {
        val runnable = object: Runnable {

            override fun run() {
                if(canRunOnMainThread()) {
                    action()
                    mainThreadRunnables.remove(this)
                }
            }
        }

        mainThreadRunnables += runnable
        mainThreadHandler.postDelayed(runnable, delayMillis.toLong())
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

    @MainThread
    override fun cancelAll() {
        super.cancelAll()
        mainThreadRunnables.forEach { mainThreadHandler.removeCallbacks(it) }
        mainThreadRunnables.clear()
    }
}