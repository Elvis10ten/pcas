package com.fluentbuild.pcas.async

interface Watcher<ValueT> {

    fun watch(consumer: (ValueT) -> Unit): Cancellable
}