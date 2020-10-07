package com.fluentbuild.pcas.async

import com.fluentbuild.pcas.logs.getLog

fun interface Cancellable {
    fun cancel()
}

internal object SentinelCancellable: Cancellable {
    override fun cancel() {}
}

class Cancellables: Cancellable {

    private val log = getLog()
    private val cancellables = mutableListOf<Cancellable>()

    operator fun plusAssign(cancellable: Cancellable) {
        cancellables += cancellable
    }

    operator fun plusAssign(cancellableList: List<Cancellable>) {
        cancellables += cancellableList
    }

    override fun cancel() {
        cancellables.forEach {
            try {
                it.cancel()
            } catch (e: Exception) {
                log.error(e) { "Error cancelling cancellable" }
            }
        }

        cancellables.clear()
    }
}