package com.fluentbuild.pcas.services

import android.graphics.Color
import com.fluentbuild.pcas.R

class ServicesProvider(
    val audioServiceId: Int,
    val mouseServiceId: Int,
    val keypadServiceId: Int,
    val healthServiceId: Int
) {

    fun getAudioService(): ServiceUiModel {
        return ServiceUiModel(
            id = audioServiceId,
            name = R.string.serviceAudio,
            icon = R.drawable.ic_headset,
            iconBackgroundColor = Color.parseColor("#FEF7DF"),
            iconColor = Color.parseColor("#F9B538"),
            state = ServiceUiModel.BondState.HAS_A_BOND
        )
    }

    fun getMouseService(): ServiceUiModel {
        return ServiceUiModel(
            id = mouseServiceId,
            name = R.string.serviceMouse,
            icon = R.drawable.ic_mouse,
            iconBackgroundColor = Color.parseColor("#E6F4EA"),
            iconColor = Color.parseColor("#1C8E3C"),
            state = ServiceUiModel.BondState.IN_PROGRESS
        )
    }

    fun getKeypadService(): ServiceUiModel {
        return ServiceUiModel(
            id = keypadServiceId,
            name = R.string.serviceKeypad,
            icon = R.drawable.ic_keyboard,
            iconBackgroundColor = Color.parseColor("#E7F1FE"),
            iconColor = Color.parseColor("#1170E8"),
            state = ServiceUiModel.BondState.NO_BONDS
        )
    }

    fun getHealthService(): ServiceUiModel {
        return ServiceUiModel(
            id = healthServiceId,
            name = R.string.serviceHealth,
            icon = R.drawable.ic_heart,
            iconBackgroundColor = Color.parseColor("#FCE8E6"),
            iconColor = Color.parseColor("#D92719"),
            state = ServiceUiModel.BondState.IN_PROGRESS
        )
    }
}