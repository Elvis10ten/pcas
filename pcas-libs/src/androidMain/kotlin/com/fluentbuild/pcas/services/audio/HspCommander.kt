package com.fluentbuild.pcas.services.audio

import android.bluetooth.BluetoothProfile
import android.content.Context
import com.fluentbuild.pcas.bluetooth.BluetoothPeripheralCommander
import com.fluentbuild.pcas.bluetooth.BluetoothProfileHolder

internal class HspCommander(
    context: Context,
    profileHolder: BluetoothProfileHolder
): BluetoothPeripheralCommander(context, profileHolder, BluetoothProfile.HEADSET)