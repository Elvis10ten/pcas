package com.fluentbuild.pcas.services.audio

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothProfile

class HspConnector(
    profileHolder: BluetoothProfileHolder,
    bluetoothAdapter: BluetoothAdapter,
): BluetoothProfileConnector(profileHolder, bluetoothAdapter, BluetoothProfile.HEADSET)