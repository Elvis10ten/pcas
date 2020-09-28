package com.fluentbuild.pcas.io

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

sealed class Address {

    /**
     * A hardware mac address, eg: "00:11:22:AA:BB:CC".
     */
    @Serializable
    data class Mac(@ProtoId(1) val macHexFormat: String): Address()

    /**
     * An IPv4 address, eg: "192.168.241.202"
     */
    @Serializable
    data class Ipv4(@ProtoId(1) val ipDottedFormat: String): Address()
}