package com.fluentbuild.pcas.bluetooth

import android.bluetooth.*
import android.os.RemoteException
import com.fluentbuild.pcas.io.Address
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.peripheral.PeripheralProfile
import com.fluentbuild.pcas.utils.VersionUtils

internal fun BluetoothAdapter.toBluetoothDevice(peripheral: Peripheral): BluetoothDevice =
    getRemoteDevice(peripheral.address.colonHex)

internal fun BluetoothDevice.toPeripheral() = Peripheral(name, Address.Mac(address))

internal fun BluetoothProfile.getPeripheralBondState(device: BluetoothDevice): PeripheralBond.State? {
    return when(getConnectionState(device)) {
        BluetoothProfile.STATE_CONNECTED -> PeripheralBond.State.CONNECTED
        BluetoothProfile.STATE_DISCONNECTED -> PeripheralBond.State.DISCONNECTED
        else -> null
    }
}

internal fun BluetoothProfile.toPeripheralProfile(): PeripheralProfile {
    return when {
        this is BluetoothA2dp -> PeripheralProfile.A2DP
        this is BluetoothHeadset -> PeripheralProfile.HSP
        VersionUtils.isAtLeastAndroidPie() && this is BluetoothHidDevice -> PeripheralProfile.HID
        else -> error("Unsupported bluetooth profile: $this")
    }
}

internal fun BluetoothProfile.isConnected(device: BluetoothDevice) =
    getPeripheralBondState(device) == PeripheralBond.State.CONNECTED

internal fun BluetoothProfile.isDisconnected(device: BluetoothDevice) =
    getPeripheralBondState(device) == PeripheralBond.State.DISCONNECTED

@Throws(RemoteException::class)
internal fun BluetoothProfile.connect(device: BluetoothDevice): Boolean {
    if(isConnected(device)) return false
    val connectMethod = javaClass.getDeclaredMethod("connect", BluetoothDevice::class.java)
    return connectMethod.invoke(this, device) as Boolean
}

@Throws(RemoteException::class)
internal fun BluetoothProfile.disconnect(device: BluetoothDevice): Boolean {
    if(isDisconnected(device)) return false
    val disconnectMethod = javaClass.getDeclaredMethod("disconnect", BluetoothDevice::class.java)
    return disconnectMethod.invoke(this, device) as Boolean
}

@Throws(RemoteException::class)
internal fun BluetoothProfile.setActiveDevice(device: BluetoothDevice?): Boolean {
    val setActiveDeviceMethod = javaClass.getDeclaredMethod("setActiveDevice", BluetoothDevice::class.java)
    return setActiveDeviceMethod.invoke(this, device) as Boolean
}