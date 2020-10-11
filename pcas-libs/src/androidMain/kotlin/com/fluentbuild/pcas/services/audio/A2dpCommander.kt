package com.fluentbuild.pcas.services.audio

import android.bluetooth.BluetoothProfile
import android.content.Context
import com.fluentbuild.pcas.bluetooth.BluetoothPeripheralCommander
import com.fluentbuild.pcas.bluetooth.BluetoothProfileHolder

internal class A2dpCommander(
    context: Context,
    profileHolder: BluetoothProfileHolder
): BluetoothPeripheralCommander(context, profileHolder, BluetoothProfile.A2DP)