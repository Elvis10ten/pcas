package com.fluentbuild.pcas.peripheral

import com.fluentbuild.pcas.io.Address
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
data class Peripheral(
    @ProtoId(1)
    val name: String = "unknown",
    @ProtoId(2)
    val address: Address.Mac
)