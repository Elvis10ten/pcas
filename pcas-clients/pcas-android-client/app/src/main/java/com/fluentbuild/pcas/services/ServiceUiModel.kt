package com.fluentbuild.pcas.services

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ServiceUiModel(
    val service: PeripheralService,
    @StringRes
    val name: Int,
    @DrawableRes
    val icon: Int,
    @ColorInt
    val iconBackgroundColor: Int,
    @ColorInt
    val iconColor: Int,
    val state: BondState
) {

    enum class BondState {
        HAS_A_BOND,
        IN_PROGRESS,
        NO_BONDS
    }
}