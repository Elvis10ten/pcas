package com.fluentbuild.pcas.console

class ConsoleItemProvider {

    fun get(): Collection<ConsoleItemUiModel> {
        return listOf(
            ConsoleItemUiModel("onStarted", ConsoleItemUiModel.Type.INFO),
            ConsoleItemUiModel("Connecting...", ConsoleItemUiModel.Type.INFO),
            ConsoleItemUiModel("Sony WH-1000XM3 A2DP connection established!", ConsoleItemUiModel.Type.INFO),
            ConsoleItemUiModel("Sony WH-1000XM3 HSP connection established!", ConsoleItemUiModel.Type.INFO),
            ConsoleItemUiModel("ConnectionException(Failed to connect to keypad)", ConsoleItemUiModel.Type.ERROR),
            ConsoleItemUiModel("Retrying connection attempt", ConsoleItemUiModel.Type.WARN),
            ConsoleItemUiModel("ConnectionException(Failed to connect to keypad)", ConsoleItemUiModel.Type.ERROR),
            ConsoleItemUiModel("Skipping keypad request", ConsoleItemUiModel.Type.WARN),
            ConsoleItemUiModel("Connected to external mouse", ConsoleItemUiModel.Type.INFO),
            ConsoleItemUiModel("On packet received", ConsoleItemUiModel.Type.INFO),
            ConsoleItemUiModel("Updating ledger...", ConsoleItemUiModel.Type.INFO),
            ConsoleItemUiModel("Ledger updated", ConsoleItemUiModel.Type.INFO)
        )
    }
}