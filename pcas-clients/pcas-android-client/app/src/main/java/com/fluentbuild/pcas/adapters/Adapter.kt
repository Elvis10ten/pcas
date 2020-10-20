package com.fluentbuild.pcas.adapters

import com.fluentbuild.pcas.EngineState
import com.fluentbuild.pcas.models.Model

fun interface Adapter<ModelT: Model> {

    fun buildModel(state: EngineState): ModelT
}