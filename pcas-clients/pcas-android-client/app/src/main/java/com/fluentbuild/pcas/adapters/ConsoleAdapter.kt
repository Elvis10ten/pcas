package com.fluentbuild.pcas.adapters

import com.fluentbuild.pcas.AppState
import com.fluentbuild.pcas.models.ConsoleModel

class ConsoleAdapter: Adapter<ConsoleModel> {

    private var currentLineIndex = 0
    private val linesBuilder = StringBuilder()

    override fun toModel(state: AppState) = state.richLogLines.createModel()

    private fun List<String>.createModel(): ConsoleModel {
        if(currentLineIndex > size) {
            linesBuilder.clear()
            currentLineIndex = 0
        }

        for(i in currentLineIndex..lastIndex) {
            linesBuilder.append(get(i))
        }

        currentLineIndex = size
        return ConsoleModel(linesBuilder.toString())
    }
}