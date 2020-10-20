package com.fluentbuild.pcas.adapters

import android.content.Context
import com.fluentbuild.pcas.Engine
import com.fluentbuild.pcas.EngineState
import com.fluentbuild.pcas.R
import com.fluentbuild.pcas.actions.EngineManualStartAction
import com.fluentbuild.pcas.actions.EngineStopAction
import com.fluentbuild.pcas.models.EngineStatusModel
import com.fluentbuild.pcas.widgets.views.createColorStateList
import com.fluentbuild.pcas.widgets.views.getDrawableCompat

class EngineStatusAdapter(private val context: Context): Adapter<EngineStatusModel> {

    override fun buildModel(state: EngineState) = state.engineStatus.buildModel()

    private fun Engine.Status.buildModel(): EngineStatusModel {
        return when(this) {
            Engine.Status.RUNNING -> {
                EngineStatusModel(
                    icon = context.getDrawableCompat(R.drawable.ic_stop_white),
                    backgroundTint = context.createColorStateList(R.color.colorDanger),
                    shouldAnimate = true,
                    onClicked = { EngineStopAction.perform(context) }
                )
            }
            Engine.Status.IDLE -> {
                EngineStatusModel(
                    icon = context.getDrawableCompat(R.drawable.ic_play_arrow_white),
                    backgroundTint = context.createColorStateList(R.color.colorSecondary),
                    shouldAnimate = false,
                    onClicked = { EngineManualStartAction.perform(context) }
                )
            }
        }
    }
}