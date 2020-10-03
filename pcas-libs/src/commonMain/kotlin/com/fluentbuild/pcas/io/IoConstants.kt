package com.fluentbuild.pcas.io

object IoConstants {

	const val MAX_PACKET_SIZE_BYTES = 16 * 1024 // 16KB

	const val MULTICAST_TTL = 255
	const val MULTICAST_PORT = 49137
	val MULTICAST_ADDRESS = Address.Ipv4("225.139.089.176")
}