package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.peripheral.Peripheral

interface PeripheralConnector {

    fun perform(action: Action)

    fun release()

    sealed class Action(val peripheral: Peripheral) {

        class Connect(peripheral: Peripheral): Action(peripheral)

        class Disconnect(peripheral: Peripheral): Action(peripheral)
    }
}