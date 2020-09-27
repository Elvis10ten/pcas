package com.fluentbuild.pcas.android

import android.Manifest
import android.app.NotificationManager
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.projection.MediaProjectionManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.PowerManager
import android.telephony.TelephonyManager

val Context.connectivityManager
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

val Context.wifiManager
    get() = getSystemService(Context.WIFI_SERVICE) as WifiManager

val Context.powerManager
    get() = getSystemService(Context.POWER_SERVICE) as PowerManager

val Context.audioManager
    get() = getSystemService(Context.AUDIO_SERVICE) as AudioManager

val Context.telephonyManager
    get() = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

val Context.bluetoothManager
    get() = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

val Context.notificationManager
    get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

val Context.mediaProjectionManager
    get() = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

fun Context.hasPermission(permission: String) =
    PackageManager.PERMISSION_GRANTED == checkCallingOrSelfPermission(permission)