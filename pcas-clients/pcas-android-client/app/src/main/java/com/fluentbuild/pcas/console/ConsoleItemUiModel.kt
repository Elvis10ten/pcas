package com.fluentbuild.pcas.console

data class ConsoleItemUiModel(
    val text: String,
    val type: Type
) {

    fun getRichText() = "<font color='${type.colorHex}'>$ $text</font>"

    enum class Type(val colorHex: String) {
        INFO("#6272a4"),
        WARN("#ffb86c"),
        ERROR("#ff5555")
    }
}