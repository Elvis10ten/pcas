package com.fluentbuild.pcas.middleware.conflicts

import com.fluentbuild.pcas.ledger.Block
import com.fluentbuild.pcas.ledger.models.Entry

data class Conflict<EntityT: Block>(
    val self: Entry<EntityT>,
    val others: Set<Entry<EntityT>>
)