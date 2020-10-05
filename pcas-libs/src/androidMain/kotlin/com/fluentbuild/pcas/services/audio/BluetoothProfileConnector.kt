package com.fluentbuild.pcas.services.audio

import android.content.Context
import com.fluentbuild.pcas.android.bluetoothAdapter
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.SentinelCancellable
import com.fluentbuild.pcas.peripheral.PeripheralConnector
import com.fluentbuild.pcas.peripheral.PeripheralConnector.Action
import com.fluentbuild.pcas.logs.getLog

abstract class BluetoothProfileConnector(
    private val context: Context,
    private val profileHolder: BluetoothProfileHolder,
    private val profileId: Int
): PeripheralConnector {

    private val log = getLog()
    private var cancellable: Cancellable = SentinelCancellable

    override fun perform(action: Action) {
        log.debug(::perform, action)
        cancellable.cancel()

        cancellable = profileHolder.useProfile(profileId) { profile ->
            val bluetoothDevice = context.bluetoothAdapter.toBluetoothDevice(action.peripheral)
            log.debug { "Performing action: $action" }

            val actionInitiated = when(action) {
                is Action.Connect -> profile.connect(bluetoothDevice)
                is Action.Disconnect -> profile.disconnect(bluetoothDevice)
            }
            log.info { "Action initiated: $actionInitiated" }
        }
    }

    override fun release() {
        log.debug(::release)
        cancellable.cancel()
        cancellable = SentinelCancellable
        profileHolder.clearCache(profileId)
    }
}