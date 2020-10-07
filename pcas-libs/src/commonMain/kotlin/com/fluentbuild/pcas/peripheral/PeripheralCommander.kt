package com.fluentbuild.pcas.peripheral

interface PeripheralCommander {

    fun perform(command: Command)

    fun release()

    sealed class Command {

        abstract val peripheral: Peripheral

        class Connect(override val peripheral: Peripheral): Command()

        class Disconnect(override val peripheral: Peripheral): Command()
    }
}