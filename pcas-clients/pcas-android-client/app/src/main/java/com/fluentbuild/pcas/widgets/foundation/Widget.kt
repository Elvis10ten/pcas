package com.fluentbuild.pcas.widgets.foundation

import com.fluentbuild.pcas.adapters.Adapter
import com.fluentbuild.pcas.models.Model

interface Widget<ModelT: Model> {

    fun update(model: ModelT)

    val adapter: Adapter<ModelT>
}