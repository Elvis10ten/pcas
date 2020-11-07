package com.fluentbuild.pcas.peripheral

import android.bluetooth.BluetoothDevice
import android.content.Context
import com.fluentbuild.pcas.io.Address
import com.fluentbuild.pcas.services.ServiceClass
import com.fluentbuild.pcas.utils.bluetoothAdapter
import com.fluentbuild.pcas.utils.mapSet
import com.fluentbuild.pcas.utils.toUuid

class PeripheralRepositoryAndroid(private val context: Context): PeripheralRepository {

	private val pairedDevices get() = context.bluetoothAdapter.bondedDevices ?: emptySet()

	override fun getPeripherals(serviceClass: ServiceClass): Set<Peripheral> {
		return pairedDevices
			.filter { serviceClass.isPeripheralSupported(it.deviceUuids) }
			.mapSet { it.toPeripheral() }
	}

	private fun BluetoothDevice.toPeripheral() =
		Peripheral(name, Address.Mac(address), StaticConfig.isConcurrencySupported(name))

	private val BluetoothDevice.deviceUuids get() = uuids?.mapSet { it.uuid.toUuid() } ?: emptySet()
}