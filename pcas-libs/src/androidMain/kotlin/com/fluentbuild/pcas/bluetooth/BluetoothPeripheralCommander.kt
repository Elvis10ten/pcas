package com.fluentbuild.pcas.bluetooth

import android.content.Context
import com.fluentbuild.pcas.utils.bluetoothAdapter
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.SentinelCancellable
import com.fluentbuild.pcas.peripheral.PeripheralCommander
import com.fluentbuild.pcas.peripheral.PeripheralCommander.Command
import com.fluentbuild.pcas.logs.getLog

internal class BluetoothPeripheralCommander(
    private val context: Context,
    private val profileHolder: BluetoothProfileHolder,
    private val androidProfileId: Int
): PeripheralCommander {

    private val log = getLog()
    private var cancellable: Cancellable = SentinelCancellable

    override var retryInfo: PeripheralCommander.RetryInfo? = null

    override fun perform(command: Command, retryCount: Int) {
        log.debug(::perform, command, retryCount)
        cancellable.cancel()
        retryInfo = PeripheralCommander.RetryInfo(command, retryCount)

        cancellable = profileHolder.useProfile(androidProfileId) { profile ->
            val bluetoothDevice = context.bluetoothAdapter.toBluetoothDevice(command.peripheral)
            log.debug { "Performing command: $command" }

            val commandInitiated = when(command) {
                is Command.Connect -> profile.connect(bluetoothDevice)
                is Command.Disconnect -> profile.disconnect(bluetoothDevice)
            }

            log.info { "Command initiated? $commandInitiated" }
        }
    }

    override fun release() {
        log.debug(::release)
        cancellable.cancel()
        cancellable = SentinelCancellable
        retryInfo = null
        profileHolder.clearCache(androidProfileId)
    }
}