package com.fluentbuild.pcas.models

import com.fluentbuild.pcas.peripheral.Peripheral

data class PeripheralModel(
    val peripheral: Peripheral,
    val isSelected: Boolean
): Model {

    val name = peripheral.name

    val address = peripheral.address
}