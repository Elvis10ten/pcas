package com.fluentbuild.pcas.peripheral

import android.bluetooth.BluetoothDevice
import android.content.Context
import com.fluentbuild.pcas.services.BluetoothUuid
import com.fluentbuild.pcas.io.Address
import com.fluentbuild.pcas.services.ServiceClass
import com.fluentbuild.pcas.utils.bluetoothAdapter
import com.fluentbuild.pcas.utils.mapSet
import com.fluentbuild.pcas.utils.toUuid

class PeripheralRepositoryAndroid(private val context: Context): PeripheralRepository {

	private val bluetoothAdapter get() = context.bluetoothAdapter

	override fun getPeripherals(serviceClass: ServiceClass): Set<Peripheral> {
		return bluetoothAdapter.bondedDevices
			.filter { device ->
				val deviceUuids = device.uuids
				if(deviceUuids == null) {
					device.fetchUuidsWithSdp()
					return@filter false
				}

				serviceClass.isPeripheralSupported(deviceUuids.mapSet { it.uuid.toUuid() })
			}
			.mapSet { it.toPeripheral() }
	}

	override fun getAudioPeripherals(): Set<Peripheral> {
		return bluetoothAdapter.bondedDevices
			.filter { it.supportsHalfDuplexAudio() && it.supportsFullDuplexAudio() }
			.mapSet { it.toPeripheral() }
	}

	override fun getHumanInterfacePeripherals(): Set<Peripheral> {
		return bluetoothAdapter.bondedDevices
			.filter { it.supportsHumanInterface() }
			.mapSet { it.toPeripheral() }
	}

	private fun BluetoothDevice.supportsHalfDuplexAudio() = supports(BluetoothUuid.A2DP)

	private fun BluetoothDevice.supportsFullDuplexAudio() =
		supports(BluetoothUuid.HSP) || supports(BluetoothUuid.HFP)

	private fun BluetoothDevice.supportsHumanInterface() = supports(BluetoothUuid.HID)

	private fun BluetoothDevice.supports(profile: BluetoothUuid): Boolean {
		return uuids.map { it.uuid.toUuid() }
			.contains(profile.uuid)
	}

	private fun BluetoothDevice.toPeripheral() = Peripheral(name, Address.Mac(address))
}