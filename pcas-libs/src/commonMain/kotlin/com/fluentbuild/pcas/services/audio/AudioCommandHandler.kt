package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.services.audio.PeripheralConnector.Action
import com.fluentbuild.pcas.middleware.ResolutionHandler
import com.fluentbuild.pcas.middleware.Conflict
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.peripheral.audio.AudioProfile

class AudioCommandHandler(
    private val audioPeripheral: Peripheral,
    private val a2dpConnector: PeripheralConnector,
    private val hspConnector: PeripheralConnector,
    private val audioRouterClient: AudioRouterClient
): ResolutionHandler {

    override fun handle(resolution: Conflict.Resolution) {
        // todo: handle throttling
        val audioProfile = AudioProfile.from(resolution.bondId)
        when(resolution) {
            is Conflict.Resolution.Connect -> {
                stopRouting()
                connect(audioProfile)
            }
            is Conflict.Resolution.Disconnect -> {
                stopRouting()
                disconnect(audioProfile)
            }
            is Conflict.Resolution.Stream -> {
                startRouting(resolution.other!!)
            }
            is Conflict.Resolution.Ambiguous -> {}
        }
    }

    override fun release() {
        stopRouting()
        a2dpConnector.release()
        hspConnector.release()
    }

    private fun startRouting(remoteSink: HostInfo) {
        audioRouterClient.start(remoteSink)
    }

    private fun stopRouting() {
        audioRouterClient.stop()
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