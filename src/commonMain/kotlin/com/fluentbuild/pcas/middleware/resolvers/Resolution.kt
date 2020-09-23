package com.fluentbuild.pcas.middleware.resolvers

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.ledger.models.BondId
import com.fluentbuild.pcas.ledger.models.Entity
import com.fluentbuild.pcas.ledger.models.ServiceId

sealed class Resolution(
    val serviceId: ServiceId,
    val bondId: BondId
) {

    override fun toString() =
        "{ name: ${this::class.simpleName}, serviceId: $serviceId, bondId: $bondId }"

    override fun equals(other: Any?): Boolean {
        if(other !is Resolution) return false
        return serviceId == other.serviceId && bondId == other.bondId
    }

    override fun hashCode(): Int {
        var result = serviceId
        result = 31 * result + bondId
        return result
    }

    class Connect(entity: Entity): Resolution(entity.serviceId, entity.bondId)

    class Disconnect(entity: Entity): Resolution(entity.serviceId, entity.bondId)

    class ShareSink(
        entity: Entity,
        val remoteSource: HostInfo
    ): Resolution(entity.serviceId, entity.bondId)

    class RouteSource(
        entity: Entity,
        val remoteSink: HostInfo
    ): Resolution(entity.serviceId, entity.bondId)

    class Ambiguous(entity: Entity): Resolution(entity.serviceId, entity.bondId)

    class Nothing(entity: Entity): Resolution(entity.serviceId, entity.bondId)
}