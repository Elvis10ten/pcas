package com.fluentbuild.pcas.utils

import com.fluentbuild.pcas.logs.Logger

internal fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

internal fun Any.logger() = lazy { Logger(this::class.simpleName!!) }