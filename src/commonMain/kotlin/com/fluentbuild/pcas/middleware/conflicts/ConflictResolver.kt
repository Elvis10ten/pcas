package com.fluentbuild.pcas.middleware.conflicts

import com.fluentbuild.pcas.ledger.models.Entity
import com.fluentbuild.pcas.ledger.models.Ledger
import com.fluentbuild.pcas.middleware.Command

interface ConflictResolver<EntityT: Entity> {

    fun resolve(ledger: Ledger, conflicts: Set<Conflict<EntityT>>): Set<Command>
}