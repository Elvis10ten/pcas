package com.fluentbuild.pcas.di

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.media.AudioManager
import android.telephony.TelephonyManager

class AndroidModule(
    internal val appContext: Context
) {

    private val bluetoothManager: BluetoothManager
        get() = appContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    internal val telephonyManager: TelephonyManager
        get() = appContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    internal val audioManager: AudioManager
        get() = appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    internal val bluetoothAdapter: BluetoothAdapter
        get() = bluetoothManager.adapter
}