package com.fluentbuild.pcas.actions

import android.content.Context
import com.fluentbuild.pcas.MainService

object StopEngineHardAction: Action {

    override fun perform(context: Context) {
        MainService.stop(context, MainService.Power.HARD)
    }
}