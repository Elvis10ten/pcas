package com.fluentbuild.pcas.android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.fluentbuild.pcas.utils.logger

class InteractivityReceiver(
    private val context: Context,
    private val onChanged: () -> Unit
): BroadcastReceiver() {

    private val log by logger()

    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action == Intent.ACTION_SCREEN_ON || intent.action == Intent.ACTION_SCREEN_OFF) {
            log.debug(::onReceive, intent)
            onChanged()
        }
    }

    fun register() {
        IntentFilter().let {
            it.addAction(Intent.ACTION_SCREEN_ON)
            it.addAction(Intent.ACTION_SCREEN_OFF)
            context.registerReceiver(this, it)
        }
    }

    fun unregister() {
        context.unregisterReceiver(this)
    }
}