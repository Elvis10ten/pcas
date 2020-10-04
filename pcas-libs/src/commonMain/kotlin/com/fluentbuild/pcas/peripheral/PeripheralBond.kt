package com.fluentbuild.pcas.peripheral

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
data class PeripheralBond(
	@ProtoNumber(1)
    val peripheral: Peripheral,
	@ProtoNumber(2)
    val bondId: BondId,
	@ProtoNumber(3)
    val state: State
) {

    override fun equals(other: Any?): Boolean {
        if(other !is PeripheralBond) return false
        return peripheral == other.peripheral && bondId == other.bondId
    }

    override fun hashCode(): Int {
        var result = peripheral.hashCode()
        result = 31 * result + bondId
        return result
    }

    enum class State {
        CONNECTING,
        CONNECTED,
        DISCONNECTING,
        DISCONNECTED
    }
}