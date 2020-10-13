package com.fluentbuild.pcas.values

import com.fluentbuild.pcas.async.Cancellable

/**
 * A simple Observable that emits [ValueT] to an observer.
 *
 * Observables are cold (they only start emitting values when you subscribe to them).
 *
 * Observables do not emit or throw an exception (unless something really bad happens).
 *
 * Observables are required to emit the current value immediately after they are subscribed to.
 */
fun interface Observable<ValueT> {

    fun subscribe(observer: (ValueT) -> Unit): Cancellable
}