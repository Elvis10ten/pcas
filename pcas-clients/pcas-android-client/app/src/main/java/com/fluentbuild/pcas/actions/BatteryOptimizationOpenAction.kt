package com.fluentbuild.pcas.actions

import android.content.Context
import android.content.Intent
import android.provider.Settings

object BatteryOptimizationOpenAction: Action {

    override fun perform(context: Context) {
        Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS).apply {
            context.startActivity(this)
        }
    }
}