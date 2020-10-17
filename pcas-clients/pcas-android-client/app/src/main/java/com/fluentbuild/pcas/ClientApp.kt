package com.fluentbuild.pcas

import android.app.Application
import android.content.Context

class ClientApp: Application() {

    val appComponent by lazy { AppComponent(this, BuildConfig.DEBUG) }
}

inline val Context.appComponent get() = (applicationContext as ClientApp).appComponent