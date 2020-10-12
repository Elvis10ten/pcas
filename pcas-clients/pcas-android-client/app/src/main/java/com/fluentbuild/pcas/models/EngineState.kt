package com.fluentbuild.pcas.models

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.fluentbuild.pcas.R
import com.github.ybq.android.spinkit.style.RotatingPlane

enum class EngineState {
    STARTED,
    IDLE
}

fun EngineState.getIconDrawable(context: Context): Drawable {
    return if(this == EngineState.IDLE) {
        ContextCompat.getDrawable(context, R.drawable.ic_play_arrow)!!
    } else {
        RotatingPlane().apply { start() }
    }
}

internal fun EngineState.getBackgroundColor(context: Context): ColorStateList {
    val backgroundColor = if(this == EngineState.IDLE) {
        R.color.colorSecondary
    } else {
        R.color.colorDanger
    }

    return ColorStateList.valueOf(ContextCompat.getColor(context, backgroundColor))
}