package com.fluentbuild.pcas.utils

/**
 * Returns a set containing only elements matching the given [predicate].
 */
internal inline fun <T> Set<T>.filterSet(predicate: (T) -> Boolean): Set<T> {
    val results = mutableSetOf<T>()

    for (element in this) {
        if (predicate(element)) {
            results += element
        }
    }

    return results
}

/**
 * Returns a set containing the results of applying the given [transform] function
 * to each element in the original set.
 */
internal inline fun <T, R> Set<T>.mapSet(transform: (T) -> R): Set<R> {
    val results = mutableSetOf<R>()

    for (element in this) {
        results += transform(element)
    }

    return results
}

/**
 * Returns a set containing the results of applying the given [transform] function
 * to each element in the original set (without nulls).
 */
internal inline fun <T, R> Set<T>.mapSetNotNull(transform: (T) -> R?): Set<R> {
    val results = mutableSetOf<R>()

    for (element in this) {
        transform(element)?.let { results.add(it) }
    }

    return results
}