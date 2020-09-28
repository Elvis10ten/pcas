package com.fluentbuild.pcas.host

import com.fluentbuild.pcas.io.Address

fun interface HostAddressProvider {

    fun getAddress(): Address.Ipv4
}