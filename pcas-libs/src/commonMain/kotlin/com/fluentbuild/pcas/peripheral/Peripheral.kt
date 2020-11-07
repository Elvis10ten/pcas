package com.fluentbuild.pcas.peripheral

import com.fluentbuild.pcas.io.Address
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
data class Peripheral constructor(
    @ProtoNumber(1)
    val name: String,
    @ProtoNumber(2)
    val address: Address.Mac,
    @ProtoNumber(3)
    val isConcurrencySupported: Boolean
) {

    override fun equals(other: Any?) = address == (other as? Peripheral)?.address

    override fun hashCode() = address.hashCode()
}