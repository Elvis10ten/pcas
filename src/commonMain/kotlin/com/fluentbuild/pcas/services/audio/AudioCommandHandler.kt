package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.services.audio.PeripheralConnector.Action
import com.fluentbuild.pcas.middleware.CommandHandler
import com.fluentbuild.pcas.middleware.Command
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.peripheral.audio.AudioProfile

class AudioCommandHandler(
    private val audioPeripheral: Peripheral,
    private val a2dpConnector: PeripheralConnector,
    private val hspConnector: PeripheralConnector,
    private val audioRouter: AudioRouter
): CommandHandler {

    override fun handle(command: Command) {
        val audioProfile = AudioProfile.from(command.bondId)
        when(command.action) {
            Command.Action.CONNECT -> {
                stopRouting()
                connect(audioProfile)
            }
            Command.Action.DISCONNECT -> {
                stopRouting()
                disconnect(audioProfile)
            }
            Command.Action.ROUTE -> {
                startRouting(command.other!!)
            }
            Command.Action.AMBIGUOUS -> {}
        }
    }

    override fun release() {
        stopRouting()
        a2dpConnector.release()
        hspConnector.release()
    }

    private fun startRouting(remoteSink: HostInfo) {
        audioRouter.start(remoteSink)
    }

    private fun stopRouting() {
        audioRouter.stop()
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