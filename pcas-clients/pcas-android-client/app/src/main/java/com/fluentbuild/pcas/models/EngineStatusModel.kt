package com.fluentbuild.pcas.models

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import com.fluentbuild.pcas.actions.Action

class EngineStatusModel(
    val icon: Drawable,
    val background: ColorStateList,
    val animate: Boolean,
    val action: Action
): Model