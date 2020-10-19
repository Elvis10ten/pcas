package com.fluentbuild.pcas.actions

import android.content.Context
import com.fluentbuild.pcas.Engine
import com.fluentbuild.pcas.appComponent

object RestartEngineAction: Action {

    override fun perform(context: Context) {
        val engineStatus = context.appComponent.engineStateObservable.currentState.engineStatus
        if(engineStatus == Engine.Status.RUNNING) {
            StartEngineHardAction.perform(context)
        }
    }
}