package com.fluentbuild.pcas.adapters

import android.content.Context
import com.fluentbuild.pcas.EngineState
import com.fluentbuild.pcas.R
import com.fluentbuild.pcas.actions.ShowPeripheralListAction
import com.fluentbuild.pcas.actions.ShowToastAction
import com.fluentbuild.pcas.ledger.Block
import com.fluentbuild.pcas.ledger.Ledger
import com.fluentbuild.pcas.models.ServiceClassModel
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.services.ServiceClass
import com.fluentbuild.pcas.widgets.views.asColorStateList
import com.fluentbuild.pcas.widgets.views.getDrawableCompat

class ServiceClassAdapter(
    private val context: Context,
    private val serviceClass: ServiceClass
): Adapter<ServiceClassModel> {

    private val isServiceEnabled: Boolean get() {
        return when(serviceClass) {
            ServiceClass.AUDIO -> true
            ServiceClass.KEYPAD -> false
            ServiceClass.MOUSE -> false
        }
    }

    private val serviceClickAction = {
        if(isServiceEnabled) {
            ShowPeripheralListAction(serviceClass).perform(context)
        } else {
            ShowToastAction(R.string.serviceUnsupportedError).perform(context)
        }
    }

    override fun toModel(state: EngineState): ServiceClassModel {
        val connectedBlocks = state.ledger?.connectedBlocks ?: emptyList()
        val description = state.ledger?.serviceDescription
        return createModel(connectedBlocks.size, description)
    }

    private fun createModel(connectionCount: Int, description: String?): ServiceClassModel {
        return when(serviceClass) {
            ServiceClass.AUDIO -> {
                ServiceClassModel(
                    serviceClass = ServiceClass.AUDIO,
                    isEnabled = isServiceEnabled,
                    name = context.getString(R.string.serviceAudio),
                    icon = context.getDrawableCompat(R.drawable.ic_headset),
                    iconBackgroundTintColor = "#FEF7DF".asColorStateList,
                    iconTintColor = "#F9B538".asColorStateList,
                    connectionCount = connectionCount,
                    description = description,
                    onClicked = serviceClickAction
                )
            }
            ServiceClass.KEYPAD -> {
                ServiceClassModel(
                    serviceClass = ServiceClass.MOUSE,
                    isEnabled = isServiceEnabled,
                    name = context.getString(R.string.serviceMouse),
                    icon = context.getDrawableCompat(R.drawable.ic_mouse),
                    iconBackgroundTintColor = "#E6F4EA".asColorStateList,
                    iconTintColor = "#1C8E3C".asColorStateList,
                    connectionCount = connectionCount,
                    description = description,
                    onClicked = serviceClickAction
                )
            }
            ServiceClass.MOUSE -> {
                ServiceClassModel(
                    serviceClass = ServiceClass.KEYPAD,
                    isEnabled = isServiceEnabled,
                    name = context.getString(R.string.serviceKeypad),
                    icon = context.getDrawableCompat(R.drawable.ic_keyboard),
                    iconBackgroundTintColor = "#E7F1FE".asColorStateList,
                    iconTintColor = "#1170E8".asColorStateList,
                    connectionCount = connectionCount,
                    description = description,
                    onClicked = serviceClickAction
                )
            }
        }
    }

    private inline val Ledger.connectedBlocks: List<Block> get() {
        return selfBlocks.filter {
            it.serviceClass == serviceClass && it.bondSteadyState == PeripheralBond.State.CONNECTED
        }
    }

    private inline val Ledger.serviceDescription: String get() {
        return selfBlocks
            .filter { it.serviceClass == serviceClass }
            .joinToString(
                separator = "\n",
                transform = { "${it.profile.formattedName}: ${it.bondSteadyState.formattedName}" }
            )
    }
}