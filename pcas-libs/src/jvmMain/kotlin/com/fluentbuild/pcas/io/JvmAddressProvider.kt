package com.fluentbuild.pcas.io

import com.fluentbuild.pcas.values.Provider

internal class JvmAddressProvider: Provider<Address.Ipv4> {

	override fun get() = Address.Ipv4(getPrimaryNetworkInterfaceAddress().hostAddress)
}