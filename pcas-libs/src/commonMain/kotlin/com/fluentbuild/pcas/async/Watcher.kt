package com.fluentbuild.pcas.async

fun interface Watcher<ValueT> {

    /**
     * Starts watching [ValueT]. Implementers are expected to immediately pass the current value to the [consumer].
     * [consumer] will be called subsequently as [ValueT] changes until cancel is called.
     */
    fun watch(consumer: (ValueT) -> Unit): Cancellable
}