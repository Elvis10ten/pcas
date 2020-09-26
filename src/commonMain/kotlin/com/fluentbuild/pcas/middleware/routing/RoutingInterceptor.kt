package com.fluentbuild.pcas.middleware.routing

import com.fluentbuild.pcas.ledger.findEntity
import com.fluentbuild.pcas.ledger.singleEntity
import com.fluentbuild.pcas.ledger.singleEntry
import com.fluentbuild.pcas.ledger.models.Ledger
import com.fluentbuild.pcas.middleware.Command
import com.fluentbuild.pcas.middleware.UpdateInterceptor
import com.fluentbuild.pcas.middleware.Command.Action
import com.fluentbuild.pcas.utils.mapSetNotNull

class RoutingInterceptor: UpdateInterceptor {

    override fun intercept(ledger: Ledger): Set<Command> {
        return ledger.bonds.mapSetNotNull { bond ->
            val otherHostPropForBond = ledger.othersProps.singleEntry(bond)
            val selfBondEntity = ledger.ownerBonds.findEntity(bond.entity)
            val selfPropEntity = ledger.ownerProps.singleEntity(bond.entity).entity

            when {
                selfBondEntity != null -> null
                !selfPropEntity.hasUsage -> null
                selfPropEntity.rank < otherHostPropForBond.entity.rank -> {
                    Command(selfPropEntity, Action.ROUTE, otherHostPropForBond.host)
                }
                else -> null
            }
        }
    }
}