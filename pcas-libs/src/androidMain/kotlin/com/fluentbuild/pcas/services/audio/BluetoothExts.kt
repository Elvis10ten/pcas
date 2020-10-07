package com.fluentbuild.pcas.services.audio

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothProfile
import android.os.RemoteException
import com.fluentbuild.pcas.io.Address
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.peripheral.PeripheralProfile
import com.fluentbuild.pcas.services.AndroidBluetoothProfileId
import com.fluentbuild.pcas.services.AndroidBluetoothProfileState


internal fun BluetoothAdapter.toBluetoothDevice(peripheral: Peripheral): BluetoothDevice =
    getRemoteDevice(peripheral.address.colonHex)

internal fun BluetoothDevice.toPeripheral() = Peripheral(name, Address.Mac(address))

internal fun AndroidBluetoothProfileId.toPeripheralProfile(): PeripheralProfile {
    return when(this) {
        BluetoothProfile.A2DP -> PeripheralProfile.A2DP
        BluetoothProfile.HEADSET -> PeripheralProfile.HSP
        BluetoothProfile.HID_DEVICE -> PeripheralProfile.HID
        else -> error("Invalid bluetooth profile id: $this")
    }
}

internal fun AndroidBluetoothProfileState.toPeripheralState(): PeripheralBond.State? {
    return when(this) {
        BluetoothProfile.STATE_CONNECTED -> PeripheralBond.State.CONNECTED
        BluetoothProfile.STATE_DISCONNECTED -> PeripheralBond.State.DISCONNECTED
        else -> null
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