package com.fluentbuild.pcas.actions

import android.content.Context
import com.fluentbuild.pcas.MainService

object StartEngineSoftAction: Action {

    override fun perform(context: Context) {
        MainService.start(context, MainService.Power.SOFT)
    }
}