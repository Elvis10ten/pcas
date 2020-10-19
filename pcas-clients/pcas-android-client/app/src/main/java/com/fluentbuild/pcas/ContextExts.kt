package com.fluentbuild.pcas

import android.content.Context
import android.media.projection.MediaProjectionManager

inline val Context.mediaProjectionManager
    get() = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager