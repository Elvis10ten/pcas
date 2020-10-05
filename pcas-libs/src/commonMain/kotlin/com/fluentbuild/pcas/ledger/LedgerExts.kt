package com.fluentbuild.pcas.ledger

internal fun Collection<Block>.getBlocksMaxTimestamp() = maxOfOrNull { it.timestamp } ?: INVALID_TIMESTAMP

internal const val HEARTBEAT_INTERVAL_MILLIS = 60 * 1000 // todo
internal const val HOST_TTL_MILLIS = 60 * 1000
internal const val INVALID_TIMESTAMP = -1L