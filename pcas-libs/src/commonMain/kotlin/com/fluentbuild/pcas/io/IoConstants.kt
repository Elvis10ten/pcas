package com.fluentbuild.pcas.io

internal const val OFFSET_ZERO = 0
internal const val MAX_PACKET_SIZE_BYTES = 32 * 1024 // 32KB
internal const val BUFFER_POOL_CAPACITY = 20

internal const val MULTICAST_TTL = 255
internal const val MULTICAST_PORT = 49137
internal val MULTICAST_ADDRESS = Address.Ipv4("225.139.089.176")