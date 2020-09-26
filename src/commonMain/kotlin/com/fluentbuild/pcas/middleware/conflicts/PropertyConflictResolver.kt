package com.fluentbuild.pcas.middleware.conflicts

import com.fluentbuild.pcas.ledger.models.Ledger
import com.fluentbuild.pcas.ledger.models.PropertyEntity
import com.fluentbuild.pcas.middleware.Command
import com.fluentbuild.pcas.utils.mapSet
import com.fluentbuild.pcas.middleware.Command.Action

class PropertyConflictResolver: ConflictResolver<PropertyEntity> {

    override fun resolve(ledger: Ledger, conflicts: Set<Conflict<PropertyEntity>>): Set<Command> {
        return conflicts.mapSet { conflict ->
            val maxOthersRank = conflict.others.maxOf { it.entity.rank }
            val selfEntity = conflict.self.entity

            when {
                selfEntity.rank > maxOthersRank -> Command(selfEntity, Action.CONNECT)
                selfEntity.rank < maxOthersRank -> Command(selfEntity, Action.DISCONNECT)
                selfEntity.rank == maxOthersRank -> Command(selfEntity, Action.AMBIGUOUS)
                else -> throw IllegalStateException("Impossible!")
            }
        }
    }
}