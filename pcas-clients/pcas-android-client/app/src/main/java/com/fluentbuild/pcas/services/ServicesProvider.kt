package com.fluentbuild.pcas.services

import android.graphics.Color
import com.fluentbuild.pcas.R

class ServicesProvider() {

    fun getAudioService(): ServiceUiModel {
        return ServiceUiModel(
            service = PeripheralService.AUDIO,
            name = R.string.serviceAudio,
            icon = R.drawable.ic_headset,
            iconBackgroundColor = Color.parseColor("#FEF7DF"),
            iconColor = Color.parseColor("#F9B538"),
            state = ServiceUiModel.BondState.HAS_A_BOND
        )
    }

    fun getMouseService(): ServiceUiModel {
        return ServiceUiModel(
            service = PeripheralService.MOUSE,
            name = R.string.serviceMouse,
            icon = R.drawable.ic_mouse,
            iconBackgroundColor = Color.parseColor("#E6F4EA"),
            iconColor = Color.parseColor("#1C8E3C"),
            state = ServiceUiModel.BondState.IN_PROGRESS
        )
    }

    fun getKeypadService(): ServiceUiModel {
        return ServiceUiModel(
            service = PeripheralService.KEYPAD,
            name = R.string.serviceKeypad,
            icon = R.drawable.ic_keyboard,
            iconBackgroundColor = Color.parseColor("#E7F1FE"),
            iconColor = Color.parseColor("#1170E8"),
            state = ServiceUiModel.BondState.NO_BONDS
        )
    }
}