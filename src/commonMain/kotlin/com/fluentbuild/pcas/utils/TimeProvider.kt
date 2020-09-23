package com.fluentbuild.pcas.utils

import kotlinx.datetime.Clock

class TimeProvider {

    fun currentTimeMillis() = Clock.System.now().toEpochMilliseconds()
}