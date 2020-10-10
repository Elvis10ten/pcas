package com.fluentbuild.pcas.ledger

internal fun Collection<Block>.getBlocksMaxTimestamp() = maxOfOrNull { it.timestamp } ?: NO_TIMESTAMP

internal const val NO_TIMESTAMP = 0L