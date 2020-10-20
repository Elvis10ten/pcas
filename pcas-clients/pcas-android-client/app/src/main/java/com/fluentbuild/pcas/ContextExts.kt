package com.fluentbuild.pcas

import android.bluetooth.BluetoothManager
import android.content.Context
import android.media.projection.MediaProjectionManager

inline val Context.mediaProjectionManager
    get() = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

inline val Context.bluetoothManager
    get() = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

inline val Context.bluetoothAdapter get() = bluetoothManager.adapter