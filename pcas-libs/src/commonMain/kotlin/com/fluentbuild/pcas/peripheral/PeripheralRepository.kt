package com.fluentbuild.pcas.peripheral

import com.fluentbuild.pcas.services.ServiceClass

interface PeripheralRepository {

	fun getPeripherals(serviceClass: ServiceClass): Set<Peripheral>
}