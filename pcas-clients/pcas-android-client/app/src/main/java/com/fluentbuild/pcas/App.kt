package com.fluentbuild.pcas

import android.app.Application
import android.content.Context

class App: Application() {

    lateinit var engineController: PcasBridge
        private set

    override fun onCreate() {
        super.onCreate()
        engineController = PcasBridge(this)
    }
}

internal fun Context.getApp() = (applicationContext as App)