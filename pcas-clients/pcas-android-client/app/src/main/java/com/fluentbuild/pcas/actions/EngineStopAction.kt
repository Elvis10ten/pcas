package com.fluentbuild.pcas.actions

import android.content.Context
import com.fluentbuild.pcas.MainService

object EngineStopAction: Action {

    override fun perform(context: Context) {
        MainService.stopEngine(context)
    }
}