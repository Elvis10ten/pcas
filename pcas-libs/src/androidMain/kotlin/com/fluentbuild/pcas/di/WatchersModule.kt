package com.fluentbuild.pcas.di

import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothHeadset
import android.bluetooth.BluetoothHidDevice
import android.content.Context
import android.os.Build
import android.os.Handler
import androidx.annotation.RequiresApi
import com.fluentbuild.pcas.io.Address
import com.fluentbuild.pcas.values.Provider
import com.fluentbuild.pcas.watchers.*
import com.fluentbuild.pcas.watchers.AudioPlaybackWatcher
import com.fluentbuild.pcas.watchers.BluetoothProfileStateWatcher
import com.fluentbuild.pcas.watchers.CallStateWatcher
import com.fluentbuild.pcas.watchers.InteractivityWatcher
import com.fluentbuild.pcas.watchers.NetworkAddressWatcher

internal class WatchersModule(
	private val appContext: Context,
	mainThreadHandler: Handler,
	hostAddressProvider: Provider<Address.Ipv4>
) {

	val networkAddressWatcher = NetworkAddressWatcher(appContext, mainThreadHandler, hostAddressProvider)

	val interactivityWatcher = InteractivityWatcher(appContext)

	val a2dpProfileStateWatcher =
		BluetoothProfileStateWatcher(appContext, BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED)

	val hspProfileStateWatcher =
		BluetoothProfileStateWatcher(appContext, BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)

	val hidProfileStateWatcher @RequiresApi(Build.VERSION_CODES.P) get() =
		BluetoothProfileStateWatcher(appContext, BluetoothHidDevice.ACTION_CONNECTION_STATE_CHANGED)

	val callStateWatcher = CallStateWatcher(appContext)

	val audioPlaybackWatcher = AudioPlaybackWatcher(appContext, mainThreadHandler)
}