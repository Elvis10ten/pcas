package com.fluentbuild.pcas.utils

import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty

internal inline fun <T> observable(initialValue: T, crossinline onChanged: (T) -> Unit): ReadWriteProperty<Any?, T> {
    return Delegates.observable(initialValue, { _, oldValue, newValue ->
        if(oldValue != newValue) {
            onChanged(newValue)
        }
    })
}

internal fun <T> Any.unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)