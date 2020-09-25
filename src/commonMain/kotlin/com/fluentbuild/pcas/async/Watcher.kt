package com.fluentbuild.pcas.async

interface Watcher<ValueT> {

    val currentValue: ValueT

    fun watch(consumer: (ValueT) -> Unit): Cancellable
}