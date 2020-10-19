package com.fluentbuild.pcas.models

import com.fluentbuild.pcas.actions.Action

class PeripheralListModel(
    val title: String,
    val description: String,
    val peripherals: List<PeripheralModel>,
    val isCancellable: Boolean,
    val doneButtonText: String,
    val selectAction: (PeripheralModel) -> Action
): Model