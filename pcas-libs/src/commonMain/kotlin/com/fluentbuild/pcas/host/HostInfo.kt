package com.fluentbuild.pcas.host

import com.fluentbuild.pcas.io.Address
import com.fluentbuild.pcas.io.Port
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
data class HostInfo(
    @ProtoNumber(1)
    val uuid: HostUuid,
    @ProtoNumber(2)
    val name: String,
    @ProtoNumber(3)
    val address: Address.Ipv4,
    @ProtoNumber(4)
    val port: Port,
    @ProtoNumber(5)
    val isInteractive: Boolean,
    @ProtoNumber(6)
    val minBufferSizeBytes: Int
) {

    override fun equals(other: Any?) = uuid == (other as? HostInfo)?.uuid

    override fun hashCode() = uuid.hashCode()
}