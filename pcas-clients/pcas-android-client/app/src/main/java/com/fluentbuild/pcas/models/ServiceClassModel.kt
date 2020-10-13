package com.fluentbuild.pcas.models

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.fluentbuild.pcas.services.ServiceClass

data class ServiceClassModel(
    val serviceClass: ServiceClass,
    val isEnabled: Boolean,
    @StringRes
    val name: Int,
    @DrawableRes
    val icon: Int,
    @ColorInt
    val iconBackgroundColor: Int,
    @ColorInt
    val iconColor: Int,
    val connectionCount: Int = 0
): Model