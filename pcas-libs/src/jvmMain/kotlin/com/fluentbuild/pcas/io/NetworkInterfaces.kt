package com.fluentbuild.pcas.io

import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException

internal fun Address.Ipv4.getPrimaryInterfaceWithAddress(): NetworkInterface {
	return NetworkInterface.getNetworkInterfaces()
		.asSequence()
		.first { it.hasAddress(this) }
}

private fun NetworkInterface.hasAddress(address: Address.Ipv4): Boolean {
	return inetAddresses.asSequence().any {
		!it.isLoopbackAddress && it is Inet4Address && it.hostAddress == address.quadDottedDecimal
	}
}

@Throws(SocketException::class)
internal fun getPrimaryNetworkInterfaceAddress(): InetAddress {
	return NetworkInterface.getNetworkInterfaces()
		.asSequence()
		.map { it.inetAddresses.asSequence() }
		.flatten()
		.filter { !it.isLoopbackAddress && it is Inet4Address }
		.sortedByDescending { it.isSiteLocalAddress }
		.first()
}
