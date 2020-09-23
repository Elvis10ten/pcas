package com.fluentbuild.pcas.middleware.resolvers

import com.fluentbuild.pcas.ledger.models.Entity
import com.fluentbuild.pcas.ledger.models.Entry

data class Conflict<EntityT: Entity>(
    val self: Entry<EntityT>,
    val others: Set<Entry<EntityT>>
)