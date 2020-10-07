package com.fluentbuild.pcas.peripheral

data class PeripheralBond(
    val bondId: BondId,
    val state: State
) {

    enum class State {
        CONNECTING,
        CONNECTED,
        DISCONNECTING,
        DISCONNECTED
    }
}