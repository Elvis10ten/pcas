package com.fluentbuild.pcas.models

import com.fluentbuild.pcas.actions.Action

class PeripheralListModel(
    val isCancellable: Boolean,
    val peripherals: List<PeripheralModel>,
    val selectAction: (PeripheralModel) -> Action
): Model