package com.fluentbuild.pcas.host

import com.fluentbuild.pcas.peripheral.Peripheral

interface PeripheralConnector {

    fun connect(peripheral: Peripheral)

    fun disconnect(peripheral: Peripheral)
}