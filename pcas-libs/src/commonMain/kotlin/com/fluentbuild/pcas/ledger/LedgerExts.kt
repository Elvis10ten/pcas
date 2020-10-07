package com.fluentbuild.pcas.ledger

internal fun Collection<Block>.getBlocksMaxTimestamp() = maxOfOrNull { it.timestamp } ?: INVALID_TIMESTAMP

internal const val HEARTBEAT_INTERVAL_MILLIS = 20 * 1000
internal const val HOST_TTL_MILLIS = HEARTBEAT_INTERVAL_MILLIS * 3
internal const val INVALID_TIMESTAMP = -1L