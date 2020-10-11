package com.fluentbuild.pcas.watchers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.fluentbuild.pcas.logs.getLog

internal class InteractivityWatcher(private val context: Context): Watcher<Boolean>() {

    private val log = getLog()
    private val broadcastReceiver = object: BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            log.debug(::onReceive, intent)
            onUpdated(intent.action == Intent.ACTION_SCREEN_ON)
        }
    }

    override fun registerInternal() {
        IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
            context.registerReceiver(broadcastReceiver, this)
        }
    }

    override fun unregisterInternal() {
        context.unregisterReceiver(broadcastReceiver)
    }
}