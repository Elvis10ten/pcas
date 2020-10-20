package com.fluentbuild.pcas.peripheral

class NoOpsConnectSilencer: ConnectSilencer {

	override fun start() {}

	override fun stop() {}

	override fun onBondUpdated(bond: PeripheralBond) {}

	override fun release() {}
}