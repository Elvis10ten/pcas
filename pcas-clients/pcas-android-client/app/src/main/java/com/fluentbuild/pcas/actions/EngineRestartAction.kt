package com.fluentbuild.pcas.actions

import android.content.Context
import com.fluentbuild.pcas.Engine
import com.fluentbuild.pcas.appComponent

object EngineRestartAction: Action {

    override fun perform(context: Context) {
        val engineStatus = context.appComponent.engineStateObservable.currentState.engineStatus
        if(engineStatus == Engine.Status.RUNNING) {
            EngineStartAction.perform(context)
        }
    }
}