package com.fluentbuild.pcas.actions

import android.content.Context
import com.fluentbuild.pcas.MainService
import com.fluentbuild.pcas.appComponent

object ClearLogAction: Action {

    override fun perform(context: Context) {
        context.appComponent.appStateObservable.clearLog()
    }
}