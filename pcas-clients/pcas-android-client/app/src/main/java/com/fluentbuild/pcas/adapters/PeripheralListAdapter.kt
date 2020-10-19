package com.fluentbuild.pcas.adapters

import android.content.Context
import com.fluentbuild.pcas.EngineState
import com.fluentbuild.pcas.R
import com.fluentbuild.pcas.actions.SelectPeripheralAction
import com.fluentbuild.pcas.appComponent
import com.fluentbuild.pcas.models.PeripheralListModel
import com.fluentbuild.pcas.models.PeripheralModel
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.services.ServiceClass

class PeripheralListAdapter(
    private val context: Context,
    private val serviceClass: ServiceClass
): Adapter<PeripheralListModel> {

    private val peripheralRepository get() = context.appComponent.peripheralRepository

    private val title: String get() {
        return when(serviceClass) {
            ServiceClass.AUDIO -> context.getString(R.string.selectAudioPeripheralTitle)
            ServiceClass.KEYPAD -> TODO()
            ServiceClass.MOUSE -> TODO()
        }
    }

    private fun getDescription(peripherals: List<PeripheralModel>): String {
        return when(serviceClass) {
            ServiceClass.AUDIO -> context.getString(R.string.selectAudioPeripheralDesc)
            ServiceClass.KEYPAD -> TODO()
            ServiceClass.MOUSE -> TODO()
        }
    }

    override fun toModel(state: EngineState): PeripheralListModel {
        val peripherals = peripheralRepository.getPeripherals(serviceClass)
            .map { PeripheralModel(it, state.isPeripheralSelected(it)) }


        return PeripheralListModel(
            title = title,
            description = getDescription(peripherals),
            peripherals = peripherals,
            isCancellable = state.hostConfig.hasAnyPeripheral,
            doneButtonText = state.doneButtonText,
            selectAction = { SelectPeripheralAction(serviceClass, it.peripheral) },
        )
    }

    private fun EngineState.isPeripheralSelected(peripheral: Peripheral) =
        hostConfig.peripherals[serviceClass] == peripheral

    private inline val EngineState.doneButtonText: String get() {
        return if(hostConfig.hasAnyPeripheral) {
            context.getString(R.string.selectPeripheralDoneActionEnabled)
        } else {
            context.getString(R.string.selectPeripheralDoneActionDisabled)
        }
    }
}