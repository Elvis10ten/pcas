package com.fluentbuild.pcas.adapters

import android.content.Context
import com.fluentbuild.pcas.Engine
import com.fluentbuild.pcas.EngineState
import com.fluentbuild.pcas.R
import com.fluentbuild.pcas.actions.RunEngineAction
import com.fluentbuild.pcas.actions.HardStopEngineAction
import com.fluentbuild.pcas.models.EngineStatusModel
import com.fluentbuild.pcas.utils.createColorStateList
import com.fluentbuild.pcas.utils.getDrawableCompat

class EngineStatusAdapter(private val context: Context): Adapter<EngineStatusModel> {

    override fun toModel(state: EngineState) = state.engineStatus.toModel()

    private fun Engine.Status.toModel(): EngineStatusModel {
        return when(this) {
            Engine.Status.RUNNING -> {
                EngineStatusModel(
                    icon = context.getDrawableCompat(R.drawable.ic_stop),
                    backgroundTint = context.createColorStateList(R.color.colorDanger),
                    shouldAnimate = true,
                    clickAction = HardStopEngineAction
                )
            }
            Engine.Status.IDLE -> {
                EngineStatusModel(
                    icon = context.getDrawableCompat(R.drawable.ic_play_arrow),
                    backgroundTint = context.createColorStateList(R.color.colorSecondary),
                    shouldAnimate = false,
                    clickAction = RunEngineAction
                )
            }
        }
    }
}