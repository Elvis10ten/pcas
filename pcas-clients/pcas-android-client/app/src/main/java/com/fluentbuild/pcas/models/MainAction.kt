package com.fluentbuild.pcas.models

import androidx.annotation.MenuRes
import com.fluentbuild.pcas.R

enum class MainAction(
    @MenuRes
    private val actionId: Int
) {
    CLEAR_CONSOLE(R.id.actionClearConsole),
    SETUP(R.id.actionSetup);

    companion object {

        fun from(@MenuRes actionId: Int) = values().single { it.actionId == actionId }
    }
}