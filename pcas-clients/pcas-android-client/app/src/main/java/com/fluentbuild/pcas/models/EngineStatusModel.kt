package com.fluentbuild.pcas.models

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable

class EngineStatusModel(
    val icon: Drawable,
    val backgroundTint: ColorStateList,
    val shouldAnimate: Boolean,
    val onClicked: () -> Unit
): Model