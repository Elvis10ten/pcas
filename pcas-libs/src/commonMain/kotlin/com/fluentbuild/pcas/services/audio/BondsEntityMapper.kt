package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.ledger.models.BondEntity
import com.fluentbuild.pcas.services.ServiceId
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.services.Mapper
import com.fluentbuild.pcas.utils.mapSet

class BondsEntityMapper(
    private val audioServiceId: ServiceId
): Mapper<Set<PeripheralBond>, Set<BondEntity>> {

    override fun map(from: Set<PeripheralBond>) = from.mapSet(::createBondEntity)

    private fun createBondEntity(peripheralBond: PeripheralBond) = BondEntity(
        serviceId = audioServiceId,
        bondId = peripheralBond.bondId,
        state = peripheralBond.state
    )
}