package com.fluentbuild.pcas.utils

import android.os.SystemClock

internal class AndroidTimeProvider: JvmTimeProvider() {

	override fun getElapsedRealtime() = SystemClock.elapsedRealtime()
}