package com.fluentbuild.pcas.android

import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothHeadset
import android.bluetooth.BluetoothHidDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.peripheral.PeripheralProfile

internal class BluetoothProfileStateCallback(
	private val context: Context,
	private val onChanged: (PeripheralProfile) -> Unit
): BroadcastReceiver() {

	private val log = getLog()

	override fun onReceive(context: Context, intent: Intent) {
		log.debug(::onReceive, intent)
		val action = intent.action ?: return

		val profile = when(action) {
			BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED -> PeripheralProfile.A2DP
			BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED -> PeripheralProfile.HSP
			BluetoothHidDevice.ACTION_CONNECTION_STATE_CHANGED -> PeripheralProfile.HID
			else -> return
		}

		onChanged(profile)
	}

	fun register() {
		IntentFilter().let {
			it.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED)
			it.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)
			it.addAction(BluetoothHidDevice.ACTION_CONNECTION_STATE_CHANGED)
			context.registerReceiver(this, it)
		}
	}

	fun unregister() {
		context.unregisterReceiver(this)
	}
}