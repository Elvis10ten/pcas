package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.peripheral.PeripheralCommander.Command
import com.fluentbuild.pcas.contention.ResolutionHandler
import com.fluentbuild.pcas.contention.Contention
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.peripheral.PeripheralCommander
import com.fluentbuild.pcas.peripheral.PeripheralProfile

internal class AudioResolutionHandler(
    private val audioPeripheral: Peripheral,
    private val profileCommanders: Map<PeripheralProfile, PeripheralCommander>,
    private val audioStreamer: AudioStreamer,
): ResolutionHandler {

    override fun handle(resolution: Contention.Resolution) {
        when(resolution) {
            is Contention.Resolution.Connect -> {
                audioStreamer.stop()
                resolution.selfBlock.profile.connect()
            }
            is Contention.Resolution.Disconnect -> {
                audioStreamer.stop()
                resolution.selfBlock.profile.disconnect()
            }
            is Contention.Resolution.Stream -> {
                resolution.selfBlock.profile.disconnect()
                audioStreamer.start(resolution.destination)
            }
            is Contention.Resolution.Ambiguous -> {}
        }
    }

    override fun release() {
        audioStreamer.stop()
        profileCommanders.values.forEach { it.release() }
    }

    private fun PeripheralProfile.connect() {
        profileCommanders.getValue(this).perform(Command.Connect(audioPeripheral))
    }

    private fun PeripheralProfile.disconnect() {
        profileCommanders.getValue(this).perform(Command.Disconnect(audioPeripheral))
    }
}