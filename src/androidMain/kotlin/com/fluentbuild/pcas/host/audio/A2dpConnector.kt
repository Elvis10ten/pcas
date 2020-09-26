package com.fluentbuild.pcas.host.audio

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothProfile

class A2dpConnector(
    profileHolder: BluetoothProfileHolder,
    bluetoothAdapter: BluetoothAdapter,
): BluetoothProfileConnector(profileHolder, bluetoothAdapter, BluetoothProfile.A2DP)