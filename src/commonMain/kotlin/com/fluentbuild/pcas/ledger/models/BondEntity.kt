package com.fluentbuild.pcas.ledger.models

import com.fluentbuild.pcas.peripheral.PeripheralBond
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
data class BondEntity(
    @ProtoNumber(1)
    override val serviceId: ServiceId,
    @ProtoNumber(2)
    override val bondId: BondId,
    @ProtoNumber(3)
    val state: PeripheralBond.State
): Entity() {

    val isConnectedOrConnecting = state == PeripheralBond.State.CONNECTING || state == PeripheralBond.State.CONNECTED

    val isConnected = state == PeripheralBond.State.CONNECTED
}