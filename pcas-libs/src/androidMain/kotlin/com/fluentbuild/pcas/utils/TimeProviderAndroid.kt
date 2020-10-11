package com.fluentbuild.pcas.utils

import android.os.SystemClock

internal class TimeProviderAndroid: TimeProviderJvm() {

	override fun getElapsedRealtime() = SystemClock.elapsedRealtime()
}