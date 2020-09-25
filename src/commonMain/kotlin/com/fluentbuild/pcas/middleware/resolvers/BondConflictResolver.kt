package com.fluentbuild.pcas.middleware.resolvers

import com.fluentbuild.pcas.ledger.models.BondEntity
import com.fluentbuild.pcas.ledger.models.Ledger
import com.fluentbuild.pcas.utils.mapSet

/**
 * Responsible for resolving bond conflicts on the ledger.
 *
 * Bond conflict occurs when there are at least two hosts with the same bond object
 * (i.e: both host's [BondEntity.serviceId] & [BondEntity.bondId] are equal).
 *
 * In practice this should rarely occur as peripherals usually limit the number of hosts per
 * [BondEntity.bondId] to one.
 */
internal class BondConflictResolver: Resolver<BondEntity> {

    override fun resolve(ledger: Ledger, conflicts: Set<Conflict<BondEntity>>): Set<Resolution> {
        TODO()
        /*return conflicts.mapSet { conflict ->
            val maxTimestamp = conflict.others.maxOfOrNull { it.entity.timestamp }
            val selfEntity = conflict.self.entity

            when {
                maxTimestamp == null -> Resolution.Nothing(selfEntity)
                maxTimestamp > selfEntity.timestamp -> Resolution.Disconnect(selfEntity)
                maxTimestamp < selfEntity.timestamp -> Resolution.Nothing(selfEntity) // Bond already exists
                maxTimestamp == selfEntity.timestamp -> Resolution.Ambiguous(selfEntity)
                else -> throw IllegalStateException("Impossible!")
            }
        }*/
    }
}