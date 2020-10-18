package com.fluentbuild.pcas.utils

import android.os.SystemClock

internal class TimeProviderAndroid: TimeProviderJvm() {

	override val elapsedRealtime get() = SystemClock.elapsedRealtime()
}