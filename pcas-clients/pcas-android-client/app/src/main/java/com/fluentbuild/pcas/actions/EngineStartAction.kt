package com.fluentbuild.pcas.actions

import android.content.Context
import com.fluentbuild.pcas.MainService

object EngineStartAction: Action {

    override fun perform(context: Context) {
        MainService.startEngine(context)
    }
}