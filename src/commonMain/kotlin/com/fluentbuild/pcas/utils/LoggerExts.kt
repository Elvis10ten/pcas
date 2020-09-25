package com.fluentbuild.pcas.utils

internal fun Any.logger() = lazy { Logger(this::class.simpleName!!) }