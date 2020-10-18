package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.peripheral.PeripheralCommander.Command
import com.fluentbuild.pcas.contention.ResolutionHandler
import com.fluentbuild.pcas.contention.Contention
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.peripheral.PeripheralCommander
import com.fluentbuild.pcas.peripheral.PeripheralProfile

internal class AudioResolutionHandler(
    private val audioPeripheral: Peripheral,
    private val a2dpCommander: PeripheralCommander,
    private val hspCommander: PeripheralCommander,
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
                audioStreamer.start(resolution.destination)
            }
            is Contention.Resolution.Ambiguous -> {}
        }
    }

    override fun release() {
        audioStreamer.stop()
        a2dpCommander.release()
        hspCommander.release()
    }

    private fun PeripheralProfile.connect() {
        val command = Command.Connect(audioPeripheral)
        when(this) {
            PeripheralProfile.A2DP -> a2dpCommander.perform(command)
            PeripheralProfile.HEADSET -> hspCommander.perform(command)
            PeripheralProfile.HID -> error("HID profile not supported in audio service")
        }
    }

    private fun PeripheralProfile.disconnect() {
        val command = Command.Disconnect(audioPeripheral)
        when(this) {
            PeripheralProfile.A2DP -> a2dpCommander.perform(command)
            PeripheralProfile.HEADSET -> hspCommander.perform(command)
            PeripheralProfile.HID -> error("HID profile not supported in audio service")
        }
    }
}