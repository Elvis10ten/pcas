package com.fluentbuild.pcas.peripheral

interface ConnectSilencer {

	fun start()

	fun stop()

	fun onBondUpdated(bond: PeripheralBond)

	fun release()
}