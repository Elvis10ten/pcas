package com.fluentbuild.pcas.utils

import android.os.Handler
import com.fluentbuild.pcas.async.Cancellable

class HandlerActionScheduler(
    private val handler: Handler
): ActionScheduler {

    private val activeRunnables = mutableSetOf<Runnable>()

    override fun scheduleRepeating(interval: Int, action: () -> Unit): Cancellable {
        val runnable = object: Runnable {

            override fun run() {
                action()
                if(activeRunnables.contains(this)) {
                    handler.postDelayed(this, interval.toLong())
                }
            }
        }

        handler.post(runnable, interval.toLong())

        return Cancellable {
            activeRunnables.remove(runnable)
            handler.removeCallbacks(runnable)
        }
    }
}