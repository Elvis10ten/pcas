package com.fluentbuild.pcas.peripheral

import android.bluetooth.BluetoothDevice
import android.content.Context
import com.fluentbuild.pcas.services.BluetoothProfile
import com.fluentbuild.pcas.io.Address
import com.fluentbuild.pcas.utils.bluetoothAdapter
import com.fluentbuild.pcas.utils.mapSet
import com.fluentbuild.pcas.utils.toUuid

class PeripheralRepositoryAndroid(private val context: Context): PeripheralRepository {

	private val bluetoothAdapter get() = context.bluetoothAdapter

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

	private fun BluetoothDevice.supportsHalfDuplexAudio() = supports(BluetoothProfile.A2DP)

	private fun BluetoothDevice.supportsFullDuplexAudio() =
		supports(BluetoothProfile.HSP) || supports(BluetoothProfile.HFP)

	private fun BluetoothDevice.supportsHumanInterface() = supports(BluetoothProfile.HID)

	private fun BluetoothDevice.supports(profile: BluetoothProfile): Boolean {
		return uuids.map { it.uuid.toUuid() }
			.contains(profile.uuid)
	}

	private fun BluetoothDevice.toPeripheral() = Peripheral(name, Address.Mac(address))
}