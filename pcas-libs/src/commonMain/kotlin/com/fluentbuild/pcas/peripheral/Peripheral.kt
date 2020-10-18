package com.fluentbuild.pcas.peripheral

import com.fluentbuild.pcas.io.Address
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
data class Peripheral(
    @ProtoNumber(1)
    val name: String,
    @ProtoNumber(2)
    val address: Address.Mac,
    @ProtoNumber(3)
    val maxConcurrentConnections: Int
) {

    override fun equals(other: Any?) = address == (other as? Peripheral)?.address

    override fun hashCode() = address.hashCode()
}