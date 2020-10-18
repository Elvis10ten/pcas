package com.fluentbuild.pcas.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothProfile
import com.fluentbuild.pcas.peripheral.PeripheralBond.State

private val CONNECTED_OR_CONNECTING_STATE = setOf(State.CONNECTED, State.CONNECTING)
private val DISCONNECTED_OR_DISCONNECTING_STATE = setOf(State.DISCONNECTED, State.DISCONNECTING)

private fun BluetoothProfile.isConnectedOrConnecting(device: BluetoothDevice) =
	CONNECTED_OR_CONNECTING_STATE.contains(getPeripheralBondState(device))

private fun BluetoothProfile.isDisconnectedOrDisconnecting(device: BluetoothDevice) =
	DISCONNECTED_OR_DISCONNECTING_STATE.contains(getPeripheralBondState(device))

internal fun BluetoothProfile.connect(device: BluetoothDevice): Boolean {
	if(isConnectedOrConnecting(device)) return false
	val connectMethod = javaClass.getDeclaredMethod("connect", BluetoothDevice::class.java)
	return connectMethod.invoke(this, device) as Boolean
}

internal fun BluetoothProfile.disconnect(device: BluetoothDevice): Boolean {
	if(isDisconnectedOrDisconnecting(device)) return false
	val disconnectMethod = javaClass.getDeclaredMethod("disconnect", BluetoothDevice::class.java)
	return disconnectMethod.invoke(this, device) as Boolean
}

internal fun BluetoothProfile.setActiveDevice(device: BluetoothDevice?): Boolean {
	val setActiveDeviceMethod = javaClass.getDeclaredMethod("setActiveDevice", BluetoothDevice::class.java)
	return setActiveDeviceMethod.invoke(this, device) as Boolean
}

internal fun BluetoothProfile.getPeripheralBondState(device: BluetoothDevice): State {
	return when(getConnectionState(device)) {
		BluetoothProfile.STATE_CONNECTED -> State.CONNECTED
		BluetoothProfile.STATE_CONNECTING -> State.CONNECTING
		BluetoothProfile.STATE_DISCONNECTED -> State.DISCONNECTED
		BluetoothProfile.STATE_DISCONNECTING -> State.DISCONNECTING
		else -> error("Invalid connection state: ${getConnectionState(device)}")
	}
}