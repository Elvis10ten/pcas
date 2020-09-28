package com.fluentbuild.pcas.services.audio

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothProfile
import android.os.RemoteException
import com.fluentbuild.pcas.io.Address
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.peripheral.audio.AudioProfile

fun BluetoothAdapter.toBluetoothDevice(peripheral: Peripheral): BluetoothDevice =
    getRemoteDevice(peripheral.address.macHexFormat)

fun BluetoothDevice.toPeripheral() = Peripheral(name, Address.Mac(address))

internal fun Int.bluetoothProfileIdToAudioProfile(): AudioProfile {
    return when(this) {
        BluetoothProfile.A2DP -> AudioProfile.A2DP
        BluetoothProfile.HEADSET -> AudioProfile.HSP
        else -> error("Invalid bluetooth profile id: $this")
    }
}

internal fun Int.bluetoothProfileStateToPeripheralState(): PeripheralBond.State {
    return when(this) {
        BluetoothProfile.STATE_CONNECTED -> PeripheralBond.State.CONNECTED
        BluetoothProfile.STATE_CONNECTING -> PeripheralBond.State.CONNECTING
        BluetoothProfile.STATE_DISCONNECTED -> PeripheralBond.State.DISCONNECTED
        BluetoothProfile.STATE_DISCONNECTING -> PeripheralBond.State.DISCONNECTING
        else -> error("Invalid bluetooth profile state: $this")
    }
}

@Throws(RemoteException::class)
internal fun BluetoothProfile.connect(device: BluetoothDevice): Boolean {
    val connectMethod = javaClass.getDeclaredMethod("connect", BluetoothDevice::class.java)
    return connectMethod.invoke(this, device) as Boolean
}

@Throws(RemoteException::class)
internal fun BluetoothProfile.disconnect(device: BluetoothDevice): Boolean {
    val disconnectMethod = javaClass.getDeclaredMethod("disconnect", BluetoothDevice::class.java)
    return disconnectMethod.invoke(this, device) as Boolean
}

@Throws(RemoteException::class)
internal fun BluetoothProfile.setActiveDevice(device: BluetoothDevice?): Boolean {
    val setActiveDeviceMethod = javaClass.getDeclaredMethod("setActiveDevice", BluetoothDevice::class.java)
    return setActiveDeviceMethod.invoke(this, device) as Boolean
}