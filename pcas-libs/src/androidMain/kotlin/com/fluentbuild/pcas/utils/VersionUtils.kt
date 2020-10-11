package com.fluentbuild.pcas.utils

import android.os.Build

object VersionUtils {

	fun isAtLeastAndroidTen() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
}