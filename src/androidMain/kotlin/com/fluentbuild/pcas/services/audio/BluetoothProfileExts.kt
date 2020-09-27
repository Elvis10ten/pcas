package com.fluentbuild.pcas.services.audio

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothProfile
import android.os.RemoteException

@Throws(RemoteException::class)
internal fun BluetoothProfile.connect(device: BluetoothDevice): Boolean {
    return javaClass.getDeclaredMethod("connect", BluetoothDevice::class.java).run {
        invoke(this, device) as Boolean
    }
}

@Throws(RemoteException::class)
internal fun BluetoothProfile.disconnect(device: BluetoothDevice): Boolean {
    return javaClass.getDeclaredMethod("disconnect", BluetoothDevice::class.java).run {
        invoke(this, device) as Boolean
    }
}

@Throws(RemoteException::class)
internal fun BluetoothProfile.setActiveDevice(device: BluetoothDevice?): Boolean {
    return javaClass.getDeclaredMethod("setActiveDevice", BluetoothDevice::class.java).run {
        invoke(this, device) as Boolean
    }
}