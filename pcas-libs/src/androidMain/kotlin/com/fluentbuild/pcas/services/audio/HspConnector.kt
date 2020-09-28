package com.fluentbuild.pcas.services.audio

import android.bluetooth.BluetoothProfile
import android.content.Context

class HspConnector(
    context: Context,
    profileHolder: BluetoothProfileHolder
): BluetoothProfileConnector(context, profileHolder, BluetoothProfile.HEADSET)