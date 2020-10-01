package com.fluentbuild.pcas.utils

import kotlinx.datetime.Clock

class TimeProvider(private val clock: Clock) {

    fun currentTimeMillis() = clock.now().toEpochMilliseconds()
}