package com.fluentbuild.pcas.ledger.models

interface Entity {

    val serviceId: ServiceId

    val bondId: BondId

    val timestamp: Long
}

fun Entity.isEqual(other: Any?): Boolean {
    if(other !is Entity) return false
    return serviceId == other.serviceId && bondId == other.bondId
}

fun Entity.getHashcode(): Int {
    var result = serviceId
    result = 31 * result + bondId
    return result
}