package com.fluentbuild.pcas.utils

import kotlinx.datetime.Clock

internal class TimeProvider(private val clock: Clock) {

    fun currentTimeMillis() = clock.now().toEpochMilliseconds()
}