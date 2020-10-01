package com.fluentbuild.pcas.utils

internal inline fun <T> Set<T>.filterSet(predicate: (T) -> Boolean): Set<T> =
    filterTo(mutableSetOf(), predicate)

internal inline fun <T, R> Set<T>.mapSet(transform: (T) -> R): Set<R> =
    mapTo(mutableSetOf(), transform)

internal inline fun <T, R: Any> Set<T>.mapSetNotNull(transform: (T) -> R?): Set<R> =
    mapNotNullTo(mutableSetOf(), transform)