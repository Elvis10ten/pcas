package com.fluentbuild.pcas.adapters

import com.fluentbuild.pcas.AppState
import com.fluentbuild.pcas.models.Model

fun interface Adapter<ModelT: Model> {

    fun toModel(state: AppState): ModelT
}