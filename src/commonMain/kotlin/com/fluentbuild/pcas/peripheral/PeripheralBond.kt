package com.fluentbuild.pcas.peripheral

import com.fluentbuild.pcas.ledger.models.BondId
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
data class PeripheralBond(
    @ProtoId(1)
    val peripheral: Peripheral,
    @ProtoId(2)
    val bondId: BondId,
    @ProtoId(3)
    val state: State
) {

    enum class State {
        CONNECTING,
        CONNECTED,
        DISCONNECTING,
        DISCONNECTED
    }
}