package com.fluentbuild.pcas.ledger.models

abstract class Entity {

    abstract val serviceId: ServiceId

    abstract val bondId: BondId

    final override fun equals(other: Any?): Boolean {
        if(other !is Entity) return false
        return serviceId == other.serviceId && bondId == other.bondId
    }

    final override fun hashCode(): Int {
        var result = serviceId
        result = 31 * result + bondId
        return result
    }
}