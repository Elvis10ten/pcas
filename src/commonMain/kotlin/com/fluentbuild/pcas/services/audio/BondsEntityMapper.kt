package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.ledger.models.BondEntity
import com.fluentbuild.pcas.ledger.models.ServiceId
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.utils.Mapper
import com.fluentbuild.pcas.utils.filterSet
import com.fluentbuild.pcas.utils.mapSet

class BondsEntityMapper(
    private val audioServiceId: ServiceId
): Mapper<Set<PeripheralBond>, Set<BondEntity>> {

    override fun map(from: Set<PeripheralBond>) =
        from.filterSet { it.state == PeripheralBond.State.CONNECTED }
            .mapSet(::createBondEntity)

    private fun createBondEntity(peripheralBond: PeripheralBond) = BondEntity(
        audioServiceId,
        peripheralBond.bondId
    )
}