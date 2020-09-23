package com.fluentbuild.pcas.async

interface Cancellable {
    fun cancel()
}

object SentinelCancellable: Cancellable {
    override fun cancel() {}
}

class Cancellables: Cancellable {

    private val cancellables = mutableListOf<Cancellable>()

    operator fun plusAssign(cancellable: Cancellable) {
        cancellables += cancellable
    }

    override fun cancel() {
        cancellables.forEach { it.cancel() }
        cancellables.clear()
    }
}