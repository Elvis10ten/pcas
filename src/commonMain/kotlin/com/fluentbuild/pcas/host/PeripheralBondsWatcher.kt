package com.fluentbuild.pcas.host

import com.fluentbuild.pcas.async.Watcher
import com.fluentbuild.pcas.peripheral.PeripheralBond

interface PeripheralBondsWatcher: Watcher<Set<PeripheralBond>>