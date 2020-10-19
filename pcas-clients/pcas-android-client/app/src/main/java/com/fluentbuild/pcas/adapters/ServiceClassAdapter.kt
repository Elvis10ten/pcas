package com.fluentbuild.pcas.adapters

import android.graphics.Color
import com.fluentbuild.pcas.EngineState
import com.fluentbuild.pcas.R
import com.fluentbuild.pcas.actions.Action
import com.fluentbuild.pcas.actions.SelectPeripheralAction
import com.fluentbuild.pcas.actions.ShowToastAction
import com.fluentbuild.pcas.ledger.Block
import com.fluentbuild.pcas.ledger.Ledger
import com.fluentbuild.pcas.models.ServiceClassModel
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.services.ServiceClass

class ServiceClassAdapter(private val serviceClass: ServiceClass): Adapter<ServiceClassModel> {

    private val isServiceEnabled: Boolean get() {
        return when(serviceClass) {
            ServiceClass.AUDIO -> true
            ServiceClass.KEYPAD -> false
            ServiceClass.MOUSE -> false
        }
    }

    private val serviceClickAction: Action get() {
        return if(isServiceEnabled) {
            SelectPeripheralAction(serviceClass)
        } else {
            ShowToastAction(R.string.serviceUnsupportedError)
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
                    name = R.string.serviceAudio,
                    icon = R.drawable.ic_headset,
                    iconBackgroundTintColor = Color.parseColor("#FEF7DF"),
                    iconTintColor = Color.parseColor("#F9B538"),
                    connectionCount = connectionCount,
                    description = description,
                    clickAction = serviceClickAction
                )
            }
            ServiceClass.KEYPAD -> {
                ServiceClassModel(
                    serviceClass = ServiceClass.MOUSE,
                    isEnabled = isServiceEnabled,
                    name = R.string.serviceMouse,
                    icon = R.drawable.ic_mouse,
                    iconBackgroundTintColor = Color.parseColor("#E6F4EA"),
                    iconTintColor = Color.parseColor("#1C8E3C"),
                    connectionCount = connectionCount,
                    description = description,
                    clickAction = serviceClickAction
                )
            }
            ServiceClass.MOUSE -> {
                ServiceClassModel(
                    serviceClass = ServiceClass.KEYPAD,
                    isEnabled = isServiceEnabled,
                    name = R.string.serviceKeypad,
                    icon = R.drawable.ic_keyboard,
                    iconBackgroundTintColor = Color.parseColor("#E7F1FE"),
                    iconTintColor = Color.parseColor("#1170E8"),
                    connectionCount = connectionCount,
                    description = description,
                    clickAction = serviceClickAction
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