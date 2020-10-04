package com.fluentbuild.pcas.middleware.conflicts

import com.fluentbuild.pcas.ledger.findEntity
import com.fluentbuild.pcas.ledger.findEntry
import com.fluentbuild.pcas.ledger.isOwnerWithoutBond
import com.fluentbuild.pcas.ledger.Ledger
import com.fluentbuild.pcas.middleware.Command
import com.fluentbuild.pcas.middleware.Command.Action
import com.fluentbuild.pcas.middleware.UpdateInterceptor

internal class BondConflictsResolver: UpdateInterceptor {

    override fun intercept(ledger: Ledger): Set<Command> {
        return ledger.othersBonds
            .filterSet { it.entity.isConnected && ledger.isOwnerWithoutBond(it.entity) }
            .mapSetNotNull { bond ->
                val otherHostPropForBond = ledger.othersProps.findEntry(bond) ?: return@mapSetNotNull null
                val selfPropEntity = ledger.ownerProps.findEntity(bond.entity)?.entity ?: return@mapSetNotNull null

                when {
                    !selfPropEntity.hasUsage -> null
                    selfPropEntity.getRank() < otherHostPropForBond.entity.getRank() -> {
                        Command(selfPropEntity, Action.ROUTE, otherHostPropForBond.host)
                    }
                    else -> null
                }
            }
    }
}