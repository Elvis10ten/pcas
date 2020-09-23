package com.fluentbuild.pcas.host

import com.fluentbuild.pcas.io.Address
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
data class HostInfo(
    @ProtoId(1)
    val uuid: String,
    @ProtoId(2)
    val name: String,
    @ProtoId(3)
    val ip: Address.Ipv6,
    @ProtoId(4)
    val port: Int,
    @ProtoId(5)
    val isInteractive: Boolean
) {

    override fun equals(other: Any?): Boolean {
        if(other !is HostInfo) return false
        return uuid == other.uuid
    }

    override fun hashCode() = uuid.hashCode()
}