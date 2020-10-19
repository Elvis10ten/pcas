package com.fluentbuild.pcas.models

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import com.fluentbuild.pcas.actions.Action

class EngineStatusModel(
    val icon: Drawable,
    val backgroundTint: ColorStateList,
    val shouldAnimate: Boolean,
    val clickAction: Action
): Model