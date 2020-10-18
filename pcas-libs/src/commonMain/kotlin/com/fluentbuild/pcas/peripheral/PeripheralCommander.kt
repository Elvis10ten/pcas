package com.fluentbuild.pcas.peripheral

interface PeripheralCommander {

    var retryInfo: RetryInfo?

    fun perform(command: Command, retryCount: Int = 0)

    fun release()

    sealed class Command {

        abstract val peripheral: Peripheral

        class Connect(override val peripheral: Peripheral): Command()

        class Disconnect(override val peripheral: Peripheral): Command()
    }

    data class RetryInfo(
        val lastCommand: Command,
        val retryCount: Int
    )
}