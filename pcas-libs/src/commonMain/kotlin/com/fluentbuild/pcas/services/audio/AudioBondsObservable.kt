package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.async.Observable
import com.fluentbuild.pcas.peripheral.PeripheralBond

internal interface AudioBondsObservable: Observable<Set<PeripheralBond>>