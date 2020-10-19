package com.fluentbuild.pcas.models

class PeripheralListModel(
    val title: String,
    val description: String,
    val peripherals: List<PeripheralModel>,
    val isCancellable: Boolean,
    val doneButtonText: String,
    val onSelected: (PeripheralModel) -> Unit
): Model