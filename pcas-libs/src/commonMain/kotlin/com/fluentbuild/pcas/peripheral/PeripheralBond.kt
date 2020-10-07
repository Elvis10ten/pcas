package com.fluentbuild.pcas.peripheral

data class PeripheralBond(
    val profile: PeripheralProfile,
    val state: State
) {

    enum class State {
        CONNECTING,
        CONNECTED,
        DISCONNECTING,
        DISCONNECTED
    }
}