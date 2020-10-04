package com.fluentbuild.pcas.middleware

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.peripheral.BondId
import com.fluentbuild.pcas.ledger.Block
import com.fluentbuild.pcas.services.ServiceId

data class Command(
    val serviceId: ServiceId,
    val bondId: BondId,
    val action: Action,
    val other: HostInfo? = null
) {

    constructor(entity: Block, action: Action): this(entity.serviceId, entity.bondId, action)

    constructor(entity: Block, action: Action, other: HostInfo): this(entity.serviceId, entity.bondId, action, other)

    enum class Action {
        CONNECT,
        DISCONNECT,
        ROUTE
    }
}