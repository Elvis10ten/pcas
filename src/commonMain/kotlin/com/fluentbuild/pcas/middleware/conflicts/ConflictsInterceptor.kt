package com.fluentbuild.pcas.middleware.conflicts

import com.fluentbuild.pcas.ledger.models.Entry
import com.fluentbuild.pcas.ledger.models.Ledger
import com.fluentbuild.pcas.ledger.models.PropertyEntity
import com.fluentbuild.pcas.middleware.Command
import com.fluentbuild.pcas.middleware.UpdateInterceptor
import com.fluentbuild.pcas.utils.filterSet
import com.fluentbuild.pcas.utils.mapSet

class ConflictsInterceptor: UpdateInterceptor {

    private val propResolver = PropertyConflictResolver()

    override fun intercept(ledger: Ledger): Set<Command> {
        return with(ledger) {
            propResolver.resolve(this, propsConflicts)
        }
    }

    private val Ledger.propsConflicts
        get() = ownerProps.mapSet { it.getPropConflicts(othersProps) }

    private fun Entry<PropertyEntity>.getPropConflicts(others: Set<Entry<PropertyEntity>>) = Conflict(
        self = this,
        others = others.filterSet { it.entity.hasUsage && this.entity == it.entity }
    )
}