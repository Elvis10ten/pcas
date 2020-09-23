package com.fluentbuild.pcas.host

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.host.PeripheralBondsWatcher

class AudioBondWatcher: PeripheralBondsWatcher {

    override fun watch(consumer: (Set<PeripheralBond>) -> Unit): Cancellable {
        TODO("Not yet implemented")
    }
}