package com.fluentbuild.pcas.watchers

import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.fluentbuild.pcas.logs.getLog

internal class BluetoothProfileStateWatcher(
	private val context: Context,
	private val stateChangedAction: String
): Watcher<Int>() {

	private val log = getLog()
	private val broadcastReceiver = object: BroadcastReceiver() {

		override fun onReceive(context: Context, intent: Intent) {
			log.debug(::onReceive, intent)
			onUpdated(intent.getIntExtra(BluetoothProfile.EXTRA_STATE, NO_PROFILE_STATE))
		}
	}

	override fun registerInternal() {
		IntentFilter(stateChangedAction).apply {
			context.registerReceiver(broadcastReceiver, this)
		}
	}

	override fun unregisterInternal() {
		context.unregisterReceiver(broadcastReceiver)
	}

	companion object {

		const val NO_PROFILE_STATE = -1
	}
}