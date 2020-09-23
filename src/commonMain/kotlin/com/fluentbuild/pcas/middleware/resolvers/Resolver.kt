package com.fluentbuild.pcas.middleware.resolvers

import com.fluentbuild.pcas.ledger.models.Entity
import com.fluentbuild.pcas.ledger.models.Ledger

interface Resolver<EntityT: Entity> {

    fun resolve(ledger: Ledger, conflicts: Set<Conflict<EntityT>>): Set<Resolution>
}