package com.fluentbuild.pcas.adapters

import android.content.Context
import com.fluentbuild.pcas.Engine
import com.fluentbuild.pcas.EngineState
import com.fluentbuild.pcas.R
import com.fluentbuild.pcas.actions.*
import com.fluentbuild.pcas.appComponent
import com.fluentbuild.pcas.models.EngineStatusModel
import com.fluentbuild.pcas.services.ServiceClass
import com.fluentbuild.pcas.utils.VersionUtils
import com.fluentbuild.pcas.widgets.views.createColorStateList
import com.fluentbuild.pcas.widgets.views.getDrawableCompat

class EngineStatusAdapter(private val context: Context): Adapter<EngineStatusModel> {

    private val appComponent get() = context.appComponent

    override fun toModel(state: EngineState) = state.engineStatus.toModel()

    private fun Engine.Status.toModel(): EngineStatusModel {
        return when(this) {
            Engine.Status.RUNNING -> {
                EngineStatusModel(
                    icon = context.getDrawableCompat(R.drawable.ic_stop_white),
                    backgroundTint = context.createColorStateList(R.color.colorDanger),
                    shouldAnimate = true,
                    onClicked = { StopEngineHardAction.perform(context) }
                )
            }
            Engine.Status.IDLE -> {
                EngineStatusModel(
                    icon = context.getDrawableCompat(R.drawable.ic_play_arrow_white),
                    backgroundTint = context.createColorStateList(R.color.colorSecondary),
                    shouldAnimate = false,
                    onClicked = onStartClicked
                )
            }
        }
    }

    private val onStartClicked = {
        StartEngineSoftAction.perform(context)
        if(VersionUtils.isAtLeastAndroidTen() && appComponent.mediaProjection == null) {
            RequestMediaProjectionAction.perform(context)
        } else if(!appComponent.hostConfigStore.get().hasAnyPeripheral) {
            ShowPeripheralListAction(ServiceClass.AUDIO).perform(context)
        } else {
            StartEngineHardAction.perform(context)
        }
    }
}