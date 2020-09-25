package com.fluentbuild.pcas.utils

import com.fluentbuild.pcas.async.Cancellable

interface ActionScheduler {

    fun scheduleRepeating(interval: Int, action: () -> Unit): Cancellable
}