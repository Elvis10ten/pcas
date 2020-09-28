package com.fluentbuild.pcas.utils

internal fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

internal fun Any.logger() = lazy { Logger(this::class.simpleName!!) }