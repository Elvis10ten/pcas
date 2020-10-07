package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.peripheral.PeripheralCommander.Command
import com.fluentbuild.pcas.conflicts.ResolutionHandler
import com.fluentbuild.pcas.conflicts.Conflict
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.peripheral.PeripheralCommander
import com.fluentbuild.pcas.peripheral.PeripheralProfile

internal class AudioResolutionHandler(
    private val audioPeripheral: Peripheral,
    private val a2dpCommander: PeripheralCommander,
    private val hspCommander: PeripheralCommander,
    private val audioStreamer: AudioStreamer,
): ResolutionHandler {

    override fun handle(resolution: Conflict.Resolution) {
        when(resolution) {
            is Conflict.Resolution.Connect -> {
                stopStreaming()
                resolution.selfBlock.profile.connect()
            }
            is Conflict.Resolution.Disconnect -> {
                stopStreaming()
                resolution.selfBlock.profile.disconnect()
            }
            is Conflict.Resolution.Stream -> {
                startStreaming(resolution.destination)
            }
            is Conflict.Resolution.Ambiguous -> {}
        }
    }

    override fun release() {
        stopStreaming()
        a2dpCommander.release()
        hspCommander.release()
    }

    private fun startStreaming(destination: HostInfo) {
        audioStreamer.start(destination)
    }

    private fun stopStreaming() {
        audioStreamer.stop()
    }

    private fun PeripheralProfile.connect() {
        when(this) {
            PeripheralProfile.A2DP -> a2dpCommander.perform(Command.Connect(audioPeripheral))
            PeripheralProfile.HSP -> hspCommander.perform(Command.Connect(audioPeripheral))
            PeripheralProfile.HID -> error("HID profile not supported in audio service")
        }
    }

    private fun PeripheralProfile.disconnect() {
        when(this) {
            PeripheralProfile.A2DP -> a2dpCommander.perform(Command.Disconnect(audioPeripheral))
            PeripheralProfile.HSP -> hspCommander.perform(Command.Disconnect(audioPeripheral))
            PeripheralProfile.HID -> error("HID profile not supported in audio service")
        }
    }
}