package com.fluentbuild.pcas.peripheral

interface PeripheralRepository {

	fun getAudioPeripherals(): Set<Peripheral>

	fun getHumanInterfacePeripherals(): Set<Peripheral>
}