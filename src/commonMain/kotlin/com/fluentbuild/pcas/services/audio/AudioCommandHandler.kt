package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.middleware.CommandHandler
import com.fluentbuild.pcas.middleware.Command
import com.fluentbuild.pcas.peripheral.audio.AudioProfile

class AudioCommandHandler(
    /*private val peripheral: () -> AudioPeripheral,
    private val a2dpConnector: PeripheralConnector<AudioPeripheral>,
    private val hspConnector: PeripheralConnector<AudioPeripheral>,*/
    private val audioRouter: AudioRouter
): CommandHandler {

    override fun handle(command: Command) {
        /*val audioProfile = AudioProfile.from(resolution.boundId)
        when(resolution) {
            is Resolution.Connect -> {
                stopRouting()
                connect(audioProfile)
            }
            is Resolution.Disconnect -> {
                stopRouting()
                disconnect(audioProfile)
            }
            is Resolution.ShareSink -> {
                stopRouting()
                connect(audioProfile)
            }
            is Resolution.RouteSource -> {
                disconnect(audioProfile)
                startRouting(resolution.remoteSink)
            }
            is Resolution.Ambiguous -> {}
            is Resolution.Nothing -> {}
        }*/
    }

    override fun release() {
        stopRouting()
    }

    private fun startRouting(remoteSink: HostInfo) {
        audioRouter.start(remoteSink)
    }

    private fun stopRouting() {
        audioRouter.stop()
    }

    private fun connect(audioProfile: AudioProfile) {
        /*when(audioProfile) {
            AudioProfile.A2DP -> a2dpConnector.connect(peripheral())
            AudioProfile.HSP -> hspConnector.connect(peripheral())
        }*/
    }

    private fun disconnect(audioProfile: AudioProfile) {
        /*when(audioProfile) {
            AudioProfile.A2DP -> a2dpConnector.disconnect(peripheral())
            AudioProfile.HSP -> hspConnector.disconnect(peripheral())
        }*/
    }
}