package com.fluentbuild.pcas.host

import com.fluentbuild.pcas.io.Address

interface HostAddressProvider {

    fun getHostAddress(): Address.Ipv4
}