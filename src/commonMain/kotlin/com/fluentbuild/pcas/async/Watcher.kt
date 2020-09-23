package com.fluentbuild.pcas.async

fun interface Watcher<ValueT> {

    fun watch(consumer: (ValueT) -> Unit): Cancellable
}