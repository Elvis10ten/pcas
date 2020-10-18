package com.fluentbuild.pcas.io

import com.fluentbuild.pcas.values.Provider

internal class AddressProviderJvm: Provider<Address.Ipv4> {

	override val currentValue get() = Address.Ipv4(getPrimaryNetworkInterfaceAddress().hostAddress)
}