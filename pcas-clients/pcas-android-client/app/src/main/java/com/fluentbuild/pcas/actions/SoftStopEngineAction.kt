package com.fluentbuild.pcas.actions

import android.content.Context
import com.fluentbuild.pcas.MainService

object SoftStopEngineAction: Action {

    override fun perform(context: Context) {
        MainService.stop(context, false)
    }
}