package com.fluentbuild.pcas.utils

import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.media.projection.MediaProjectionManager

inline val Context.notificationManager
    get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

inline val Context.mediaProjectionManager
    get() = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

fun Context.hasPermission(permission: String) =
    PackageManager.PERMISSION_GRANTED == checkCallingOrSelfPermission(permission)