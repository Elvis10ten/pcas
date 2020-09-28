package com.fluentbuild.pcas.middleware.routing

import com.fluentbuild.pcas.ledger.findEntity
import com.fluentbuild.pcas.ledger.findEntry
import com.fluentbuild.pcas.ledger.models.Ledger
import com.fluentbuild.pcas.middleware.Command
import com.fluentbuild.pcas.middleware.UpdateInterceptor
import com.fluentbuild.pcas.middleware.Command.Action
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.utils.filterSet
import com.fluentbuild.pcas.utils.mapSetNotNull

class RouterClientInterceptor: UpdateInterceptor {

    override fun intercept(ledger: Ledger): Set<Command> {
        return ledger.bonds.filterSet { it.entity.state == PeripheralBond.State.CONNECTED }
            .mapSetNotNull { bond ->
                val isSelfConnected = ledger.ownerBonds.any {
                    it.entity == bond.entity && it.entity.isConnectedOrConnecting
                }

                if(isSelfConnected) {
                    return@mapSetNotNull null
                }

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