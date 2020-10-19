package com.fluentbuild.pcas.actions

import android.content.Context
import com.fluentbuild.pcas.appComponent
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.services.ServiceClass

class SelectPeripheralAction(
    private val serviceClass: ServiceClass,
    private val peripheral: Peripheral
): Action {

    override fun perform(context: Context) {
        context.appComponent.hostConfigStore.setPeripheral(serviceClass, peripheral)
    }
}