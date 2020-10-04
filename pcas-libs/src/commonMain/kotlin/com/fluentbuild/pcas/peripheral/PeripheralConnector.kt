package com.fluentbuild.pcas.peripheral

interface PeripheralConnector {

    fun perform(action: Action)

    fun release()

    sealed class Action(val peripheral: Peripheral) {

        class Connect(peripheral: Peripheral): Action(peripheral)

        class Disconnect(peripheral: Peripheral): Action(peripheral)
    }
}