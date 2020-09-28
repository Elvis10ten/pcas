package com.fluentbuild.pcas.host

import com.fluentbuild.pcas.io.Address
import com.fluentbuild.pcas.io.Port
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
data class HostInfo(
    @ProtoId(1)
    val uuid: String,
    @ProtoId(2)
    val name: String,
    @ProtoId(3)
    val address: Address.Ipv4,
    @ProtoId(4)
    val port: Port,
    @ProtoId(5)
    val isInteractive: Boolean,
    @ProtoId(6)
    val minBufferSizeBytes: Int
) {

    override fun equals(other: Any?) = uuid == (other as? HostInfo)?.uuid

    override fun hashCode() = uuid.hashCode()
}