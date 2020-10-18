package com.fluentbuild.pcas.utils

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.PowerManager
import android.telephony.TelephonyManager

inline val Context.connectivityManager get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

inline val Context.wifiManager get() = getSystemService(Context.WIFI_SERVICE) as WifiManager

inline val Context.powerManager get() = getSystemService(Context.POWER_SERVICE) as PowerManager

inline val Context.audioManager get() = getSystemService(Context.AUDIO_SERVICE) as AudioManager

inline val Context.telephonyManager get() = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

inline val Context.bluetoothManager get() = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

inline val Context.bluetoothAdapter: BluetoothAdapter get() = bluetoothManager.adapter