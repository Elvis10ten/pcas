package com.fluentbuild.pcas.models

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import com.fluentbuild.pcas.actions.Action
import com.fluentbuild.pcas.services.ServiceClass

data class ServiceClassModel(
    val serviceClass: ServiceClass,
    val isEnabled: Boolean,
    val name: String,
    val icon: Drawable,
    val iconBackgroundTintColor: ColorStateList,
    val iconTintColor: ColorStateList,
    val connectionCount: Int = 0,
    val description: String? = null,
    val clickAction: Action
): Model