package com.fluentbuild.pcas.actions

import android.content.Context
import com.fluentbuild.pcas.MainService

object RunEngineAction: Action {

    override fun perform(context: Context) {
        MainService.start(context, false)
    }
}