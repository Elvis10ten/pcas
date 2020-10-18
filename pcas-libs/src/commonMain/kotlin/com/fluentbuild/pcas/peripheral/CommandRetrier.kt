package com.fluentbuild.pcas.peripheral

class CommandRetrier(private val profileCommanders: Map<PeripheralProfile, PeripheralCommander>) {

	fun onBondUpdated(bond: PeripheralBond) {
		val commander = profileCommanders.getValue(bond.profile)
		val retryInfo = commander.retryInfo ?: return

		if(retryInfo.retryCount > MAX_RETRIES) {
			return
		}

		when(retryInfo.lastCommand) {
			is PeripheralCommander.Command.Connect -> {
				if(bond.hotState == PeripheralBond.State.DISCONNECTED) {
					commander.perform(retryInfo.lastCommand, retryInfo.retryCount + 1)
				}
			}
			is PeripheralCommander.Command.Disconnect -> {
				if(bond.hotState == PeripheralBond.State.CONNECTED) {
					commander.perform(retryInfo.lastCommand, retryInfo.retryCount + 1)
				}
			}
		}
	}

	companion object {

		private const val MAX_RETRIES = 5
	}
}