package com.fluentbuild.pcas.ledger.models

import com.fluentbuild.pcas.host.HostInfo
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
data class Entry<EntityT: Entity>(
    @ProtoId(1)
    val entity: EntityT,
    @ProtoId(2)
    val host: HostInfo,
    @ProtoId(3)
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