package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.peripheral.PeripheralConnector.Action
import com.fluentbuild.pcas.middleware.ResolutionHandler
import com.fluentbuild.pcas.middleware.Conflict
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.peripheral.PeripheralConnector
import com.fluentbuild.pcas.peripheral.audio.AudioProfile

class AudioResolutionHandler(
    private val audioPeripheral: Peripheral,
    private val a2dpConnector: PeripheralConnector,
    private val hspConnector: PeripheralConnector,
    private val audioStreamer: AudioStreamer
): ResolutionHandler {

    override fun handle(resolution: Conflict.Resolution) {
        // todo: handle throttling maybe
        val audioProfile = AudioProfile.from(resolution.selfBlock.bondId)
        when(resolution) {
            is Conflict.Resolution.Connect -> {
                stopStreaming()
                connect(audioProfile)
            }
            is Conflict.Resolution.Disconnect -> {
                stopStreaming()
                disconnect(audioProfile)
            }
            is Conflict.Resolution.Stream -> {
                startStreaming(resolution.destination)
            }
            is Conflict.Resolution.Ambiguous -> {}
        }
    }

    override fun release() {
        stopStreaming()
        a2dpConnector.release()
        hspConnector.release()
    }

    private fun startStreaming(remoteSink: HostInfo) {
        audioStreamer.start(remoteSink)
    }

    private fun stopStreaming() {
        audioStreamer.stop()
    }

    private fun connect(audioProfile: AudioProfile) {
        when(audioProfile) {
            AudioProfile.A2DP -> a2dpConnector.perform(Action.Connect(audioPeripheral))
            AudioProfile.HSP -> hspConnector.perform(Action.Connect(audioPeripheral))
        }
    }

    private fun disconnect(audioProfile: AudioProfile) {
        when(audioProfile) {
            AudioProfile.A2DP -> a2dpConnector.perform(Action.Disconnect(audioPeripheral))
            AudioProfile.HSP -> hspConnector.perform(Action.Disconnect(audioPeripheral))
        }
    }
}