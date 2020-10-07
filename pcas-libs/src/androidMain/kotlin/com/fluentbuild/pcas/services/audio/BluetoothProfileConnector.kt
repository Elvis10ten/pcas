package com.fluentbuild.pcas.services.audio

import android.content.Context
import com.fluentbuild.pcas.android.bluetoothAdapter
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.SentinelCancellable
import com.fluentbuild.pcas.peripheral.PeripheralCommander
import com.fluentbuild.pcas.peripheral.PeripheralCommander.Command
import com.fluentbuild.pcas.logs.getLog

abstract class BluetoothProfileConnector(
    private val context: Context,
    private val profileHolder: BluetoothProfileHolder,
    private val profileId: Int
): PeripheralCommander {

    private val log = getLog()
    private var cancellable: Cancellable = SentinelCancellable

    override fun perform(command: Command) {
        log.debug(::perform, command)
        cancellable.cancel()

        cancellable = profileHolder.useProfile(profileId) { profile ->
            val bluetoothDevice = context.bluetoothAdapter.toBluetoothDevice(command.peripheral)
            log.debug { "Performing action: $command" }

            val actionInitiated = when(command) {
                is Command.Connect -> profile.connect(bluetoothDevice)
                is Command.Disconnect -> profile.disconnect(bluetoothDevice)
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