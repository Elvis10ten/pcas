package com.fluentbuild.pcas.peripheral

import com.fluentbuild.pcas.io.Address
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

// todo put peripheral on the wire
@Serializable
data class Peripheral(
    @ProtoNumber(1)
    val name: String = "unknown",
    @ProtoNumber(2)
    val address: Address.Mac
)