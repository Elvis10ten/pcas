package com.fluentbuild.pcas.io

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

sealed class Address {

    @Serializable
    data class Mac(@ProtoNumber(1) val colonHexValue: String): Address()

    @Serializable
    data class Ipv4(@ProtoNumber(1) val quadDottedDecimalValue: String): Address()
}