package com.fluentbuild.pcas.actions

import android.content.Context
import com.fluentbuild.pcas.MainService

object StopEngineSoftAction: Action {

    override fun perform(context: Context) {
        MainService.stop(context, MainService.Power.SOFT)
    }
}