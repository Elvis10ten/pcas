package com.fluentbuild.pcas.middleware

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.ledger.models.BondId
import com.fluentbuild.pcas.ledger.models.Entity
import com.fluentbuild.pcas.ledger.models.ServiceId

data class Command(
    val serviceId: ServiceId,
    val bondId: BondId,
    val action: Action,
    val other: HostInfo? = null
) {

    constructor(entity: Entity, action: Action): this(entity.serviceId, entity.bondId, action)

    constructor(entity: Entity, action: Action, other: HostInfo): this(entity.serviceId, entity.bondId, action, other)

    enum class Action {
        CONNECT,
        DISCONNECT,
        ROUTE,
        AMBIGUOUS
    }
}