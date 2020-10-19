package com.fluentbuild.pcas.adapters

import android.graphics.Color
import com.fluentbuild.pcas.EngineState
import com.fluentbuild.pcas.R
import com.fluentbuild.pcas.ledger.Block
import com.fluentbuild.pcas.ledger.Ledger
import com.fluentbuild.pcas.models.ServiceClassModel
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.services.ServiceClass

class ServiceClassAdapter(private val widgetServiceClass: ServiceClass): Adapter<ServiceClassModel> {

    override fun toModel(state: EngineState): ServiceClassModel {
        val connectedBlocks = state.ledger?.connectedBlocks ?: emptyList()
        val description = state.ledger?.description
        return createModel(connectedBlocks.size, description)
    }

    private fun createModel(connectionCount: Int, description: String?): ServiceClassModel {
        return when(widgetServiceClass) {
            ServiceClass.AUDIO -> {
                ServiceClassModel(
                    serviceClass = ServiceClass.AUDIO,
                    isEnabled = true,
                    name = R.string.serviceAudio,
                    icon = R.drawable.ic_headset,
                    iconBackgroundTintColor = Color.parseColor("#FEF7DF"),
                    iconTintColor = Color.parseColor("#F9B538"),
                    connectionCount = connectionCount,
                    description = description
                )
            }
            ServiceClass.KEYPAD -> {
                ServiceClassModel(
                    serviceClass = ServiceClass.MOUSE,
                    isEnabled = false,
                    name = R.string.serviceMouse,
                    icon = R.drawable.ic_mouse,
                    iconBackgroundTintColor = Color.parseColor("#E6F4EA"),
                    iconTintColor = Color.parseColor("#1C8E3C"),
                    connectionCount = connectionCount,
                    description = description
                )
            }
            ServiceClass.MOUSE -> {
                ServiceClassModel(
                    serviceClass = ServiceClass.KEYPAD,
                    isEnabled = false,
                    name = R.string.serviceKeypad,
                    icon = R.drawable.ic_keyboard,
                    iconBackgroundTintColor = Color.parseColor("#E7F1FE"),
                    iconTintColor = Color.parseColor("#1170E8"),
                    connectionCount = connectionCount,
                    description = description
                )
            }
        }
    }

    private inline val Ledger.connectedBlocks: List<Block> get() {
        return selfBlocks.filter {
            it.bondSteadyState == PeripheralBond.State.CONNECTED && it.serviceClass == widgetServiceClass
        }
    }

    private inline val Ledger.description: String get() {
        return selfBlocks
            .filter { it.serviceClass == widgetServiceClass }
            .joinToString(separator = "\n", transform = {
                "${it.profile.formattedName}: ${it.bondSteadyState.formattedName}"
            })
    }
}