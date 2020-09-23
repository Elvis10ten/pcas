package com.fluentbuild.pcas.io

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

sealed class Address {

    /**
     * A hardware mac address, eg: "00:11:22:AA:BB:CC".
     */
    @Serializable
    data class Mac(@ProtoId(1) val colonHexNotation: String): Address()

    @Serializable
    data class Ipv6(@ProtoId(1) val colonHexNotation: String): Address()
}