package com.fluentbuild.pcas.utils

internal interface TimeProvider {

    fun currentTimeMillis(): Timestamp

    fun getElapsedRealtime(): ElapsedRealtime
}

internal typealias Timestamp = Long

internal typealias ElapsedRealtime = Long