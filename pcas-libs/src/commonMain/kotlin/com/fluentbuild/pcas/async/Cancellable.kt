package com.fluentbuild.pcas.async

internal fun interface Cancellable {
    fun cancel()
}

internal object SentinelCancellable: Cancellable {
    override fun cancel() {}
}

internal class Cancellables: Cancellable {

    private val cancellables = mutableListOf<Cancellable>()

    operator fun plusAssign(cancellable: Cancellable) {
        cancellables += cancellable
    }

    override fun cancel() {
        cancellables.forEach { it.cancel() }
        cancellables.clear()
    }
}