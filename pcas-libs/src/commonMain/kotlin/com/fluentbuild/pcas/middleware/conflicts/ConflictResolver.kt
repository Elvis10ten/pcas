package com.fluentbuild.pcas.middleware.conflicts

import com.fluentbuild.pcas.ledger.Block
import com.fluentbuild.pcas.ledger.Ledger
import com.fluentbuild.pcas.middleware.Command

interface ConflictResolver<EntityT: Block> {

    fun resolve(ledger: Ledger, conflicts: Set<Conflict<EntityT>>): Set<Command>
}