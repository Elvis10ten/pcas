package com.fluentbuild.pcas.models

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.fluentbuild.pcas.actions.Action
import com.fluentbuild.pcas.services.ServiceClass

data class ServiceClassModel(
    val serviceClass: ServiceClass,
    val isEnabled: Boolean,
    @StringRes
    val name: Int,
    @DrawableRes
    val icon: Int,
    @ColorInt
    val iconBackgroundTintColor: Int,
    @ColorInt
    val iconTintColor: Int,
    val connectionCount: Int = 0,
    val description: String? = null,
    val clickAction: Action
): Model