package com.fluentbuild.pcas.middleware.resolvers

import com.fluentbuild.pcas.ledger.models.BondEntity
import com.fluentbuild.pcas.ledger.models.Entry
import com.fluentbuild.pcas.ledger.models.Ledger
import com.fluentbuild.pcas.ledger.models.PropertyEntity
import com.fluentbuild.pcas.utils.filterSet
import com.fluentbuild.pcas.utils.mapSet

class ConflictsSolver {

    private val bondResolver = BondConflictResolver()
    private val propResolver = PropertyConflictResolver()

    fun resolve(ledger: Ledger) = with(ledger) {
        bondResolver.resolve(this, bondsConflicts) + propResolver.resolve(this, propsConflicts)
    }

    private val Ledger.bondsConflicts
        get() = selfBondEntries.mapSet { it.getBondConflicts(othersBondEntries) }

    private val Ledger.propsConflicts
        get() = selfPropEntries.mapSet { it.getPropConflicts(othersPropEntries) }

    private fun Entry<BondEntity>.getBondConflicts(others: Set<Entry<BondEntity>>) = Conflict(
        self = this,
        others = others.filterSet { this.entity == it.entity }
    )

    private fun Entry<PropertyEntity>.getPropConflicts(others: Set<Entry<PropertyEntity>>) = Conflict(
        self = this,
        others = others.filterSet { it.entity.hasUsage && this.entity == it.entity }
    )
}