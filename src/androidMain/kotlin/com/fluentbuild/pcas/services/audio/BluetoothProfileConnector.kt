package com.fluentbuild.pcas.services.audio

import android.bluetooth.BluetoothAdapter
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.SentinelCancellable
import com.fluentbuild.pcas.host.PeripheralConnector
import com.fluentbuild.pcas.host.PeripheralConnector.Action
import com.fluentbuild.pcas.utils.logger

abstract class BluetoothProfileConnector(
    private val profileHolder: BluetoothProfileHolder,
    private val bluetoothAdapter: BluetoothAdapter,
    private val profileId: Int
): PeripheralConnector {

    private val log by logger()
    private var cancellable: Cancellable = SentinelCancellable

    override fun perform(action: Action) {
        log.debug(::perform, action)
        cancellable.cancel()

        cancellable = profileHolder.useProfile(profileId) { profile ->
            val bluetoothDevice = bluetoothAdapter.toBluetoothDevice(action.peripheral)
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
        profileHolder.dropProfile(profileId)
    }
}