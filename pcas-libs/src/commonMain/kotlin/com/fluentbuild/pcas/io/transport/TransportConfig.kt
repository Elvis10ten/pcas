package com.fluentbuild.pcas.io.transport

import com.fluentbuild.pcas.io.Address

internal object TransportConfig {

	const val MULTICAST_TTL = 255
	const val MULTICAST_PORT = 49137
	val MULTICAST_ADDRESS = Address.Ipv4("225.139.089.176")

	const val OFFSET_ZERO = 0

	const val MAX_PARCEL_SIZE_BYTES = 24 * 1024 // 24KB
	const val PARCEL_POOL_CAPACITY = 24
}