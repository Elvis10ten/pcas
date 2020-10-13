package com.fluentbuild.pcas.adapters

import android.graphics.Color
import com.fluentbuild.pcas.AppState
import com.fluentbuild.pcas.R
import com.fluentbuild.pcas.ledger.Block
import com.fluentbuild.pcas.ledger.Ledger
import com.fluentbuild.pcas.models.ServiceClassModel
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.services.ServiceClass

class ServiceClassAdapter(private val widgetServiceClass: ServiceClass): Adapter<ServiceClassModel> {

    override fun toModel(state: AppState): ServiceClassModel {
        val connectedBlocks = state.ledger?.connectedBlocks ?: emptyList()
        return createModel(connectedBlocks.size)
    }

    private fun createModel(connectionCount: Int): ServiceClassModel {
        return when(widgetServiceClass) {
            ServiceClass.AUDIO -> {
                ServiceClassModel(
                    serviceClass = ServiceClass.AUDIO,
                    isEnabled = true,
                    name = R.string.serviceAudio,
                    icon = R.drawable.ic_headset,
                    iconBackgroundColor = Color.parseColor("#FEF7DF"),
                    iconColor = Color.parseColor("#F9B538"),
                    connectionCount = connectionCount
                )
            }
            ServiceClass.KEYPAD -> {
                ServiceClassModel(
                    serviceClass = ServiceClass.MOUSE,
                    isEnabled = false,
                    name = R.string.serviceMouse,
                    icon = R.drawable.ic_mouse,
                    iconBackgroundColor = Color.parseColor("#E6F4EA"),
                    iconColor = Color.parseColor("#1C8E3C"),
                    connectionCount = connectionCount
                )
            }
            ServiceClass.MOUSE -> {
                ServiceClassModel(
                    serviceClass = ServiceClass.KEYPAD,
                    isEnabled = false,
                    name = R.string.serviceKeypad,
                    icon = R.drawable.ic_keyboard,
                    iconBackgroundColor = Color.parseColor("#E7F1FE"),
                    iconColor = Color.parseColor("#1170E8"),
                    connectionCount = connectionCount
                )
            }
        }
    }

    private inline val Ledger.connectedBlocks: List<Block> get() {
        return selfBlocks.filter {
            it.bondState == PeripheralBond.State.CONNECTED && it.serviceClass == widgetServiceClass
        }
    }
}