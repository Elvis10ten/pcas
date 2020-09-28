package com.fluentbuild.pcas.ledger.models

import com.fluentbuild.pcas.host.HostInfo
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
data class Entry<EntityT: Entity>(
    @ProtoNumber(1)
    val entity: EntityT,
    @ProtoNumber(2)
    val host: HostInfo,
    @ProtoNumber(3)
    internal val timestamp: Long
) {

    override fun equals(other: Any?): Boolean {
        if(other !is Entry<*>) return false
        return entity == other.entity && host == other.host
    }

    override fun hashCode(): Int {
        var result = entity.hashCode()
        result = 31 * result + host.hashCode()
        return result
    }
}