package com.fluentbuild.pcas.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothProfile
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.peripheral.PeripheralProfile

internal fun BluetoothAdapter.toBluetoothDevice(peripheral: Peripheral): BluetoothDevice =
    getRemoteDevice(peripheral.address.colonHexValue)

@SuppressLint("InlinedApi")
internal fun PeripheralProfile.getAndroidProfileId(): Int {
    return when(this) {
        PeripheralProfile.A2DP -> BluetoothProfile.A2DP
        PeripheralProfile.HEADSET -> BluetoothProfile.HEADSET
        PeripheralProfile.HID -> BluetoothProfile.HID_DEVICE
    }
}