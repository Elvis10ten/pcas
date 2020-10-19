package com.fluentbuild.pcas.adapters

import android.content.Context
import com.fluentbuild.pcas.EngineState
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

    override fun toModel(state: EngineState): PeripheralListModel {
        val peripherals = peripheralRepository.getPeripherals(serviceClass)
            .map { PeripheralModel(it, state.isPeripheralSelected(it)) }

        return PeripheralListModel(
            isCancellable = state.hostConfig.peripherals.isNotEmpty(),
            peripherals = peripherals,
            selectAction = { SelectPeripheralAction(serviceClass, it.peripheral) }
        )
    }

    private fun EngineState.isPeripheralSelected(peripheral: Peripheral) =
        hostConfig.peripherals[serviceClass] == peripheral
}