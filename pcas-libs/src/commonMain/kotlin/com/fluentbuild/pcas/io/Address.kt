package com.fluentbuild.pcas.io

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

sealed class Address {

    @Serializable
    data class Mac(@ProtoNumber(1) val colonHex: String): Address()

    @Serializable
    data class Ipv4(@ProtoNumber(1) val quadDottedDecimal: String): Address()
}