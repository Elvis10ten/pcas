package com.fluentbuild.pcas.host

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothProfile
import com.fluentbuild.pcas.io.Address
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.peripheral.audio.AudioProfile

fun BluetoothAdapter.toBluetoothDevice(peripheral: Peripheral): BluetoothDevice =
    getRemoteDevice(peripheral.address.colonHexNotation)

fun BluetoothDevice.toPeripheral() = Peripheral(name, Address.Mac(address))

internal fun profileIdToAudioProfile(profileId: Int): AudioProfile {
    return when(profileId) {
        BluetoothProfile.A2DP -> AudioProfile.A2DP
        BluetoothProfile.HEADSET -> AudioProfile.HSP
        else -> error("faf")
    }
}

internal fun Int.toPeripheralState(): PeripheralBond.State {
    return when(this) {
        BluetoothProfile.STATE_CONNECTED -> PeripheralBond.State.CONNECTED
        BluetoothProfile.STATE_CONNECTING -> PeripheralBond.State.CONNECTING
        BluetoothProfile.STATE_DISCONNECTED -> PeripheralBond.State.DISCONNECTED
        BluetoothProfile.STATE_DISCONNECTING -> PeripheralBond.State.DISCONNECTING
        else -> error("faf")
    }
}