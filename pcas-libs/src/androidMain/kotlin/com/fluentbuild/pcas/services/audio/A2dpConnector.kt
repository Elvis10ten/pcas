package com.fluentbuild.pcas.services.audio

import android.bluetooth.BluetoothProfile
import android.content.Context

class A2dpConnector(
    context: Context,
    profileHolder: BluetoothProfileHolder
): BluetoothProfileConnector(context, profileHolder, BluetoothProfile.A2DP)