package com.fluentbuild.pcas.middleware.conflicts

import com.fluentbuild.pcas.ledger.models.Ledger
import com.fluentbuild.pcas.ledger.models.PropertyEntity
import com.fluentbuild.pcas.middleware.Command
import com.fluentbuild.pcas.utils.mapSet
import com.fluentbuild.pcas.middleware.Command.Action

class PropertyConflictResolver: ConflictResolver<PropertyEntity> {

    override fun resolve(ledger: Ledger, conflicts: Set<Conflict<PropertyEntity>>): Set<Command> {
        return conflicts.mapSet { conflict ->
            val selfEntity = conflict.self.entity
            if(conflict.others.isEmpty()) return@mapSet Command(selfEntity, Action.CONNECT)
            val maxOthersRank = conflict.others.maxOf { it.entity.getRank() }

            when {
                selfEntity.getRank() > maxOthersRank -> Command(selfEntity, Action.CONNECT)
                selfEntity.getRank() < maxOthersRank -> Command(selfEntity, Action.DISCONNECT)
                selfEntity.getRank() == maxOthersRank -> Command(selfEntity, Action.AMBIGUOUS)
                else -> throw IllegalStateException("Impossible!")
            }
        }
    }
}