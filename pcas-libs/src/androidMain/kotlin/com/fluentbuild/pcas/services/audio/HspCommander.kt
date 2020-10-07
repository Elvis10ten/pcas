package com.fluentbuild.pcas.services.audio

import android.bluetooth.BluetoothProfile
import android.content.Context

internal class HspCommander(
    context: Context,
    profileHolder: BluetoothProfileHolder
): BluetoothPeripheralCommander(context, profileHolder, BluetoothProfile.HEADSET)