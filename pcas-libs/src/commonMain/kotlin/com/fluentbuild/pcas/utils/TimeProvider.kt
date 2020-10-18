package com.fluentbuild.pcas.utils

internal interface TimeProvider {

    val currentTimeMillis: Timestamp

    val elapsedRealtime: ElapsedRealtime
}

internal typealias Timestamp = Long

internal typealias ElapsedRealtime = Long