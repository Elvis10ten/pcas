package com.fluentbuild.pcas.widgets

import android.view.View
import com.fluentbuild.pcas.appComponent
import com.fluentbuild.pcas.models.Model
import com.fluentbuild.pcas.utils.Delegates.observable
import com.fluentbuild.pcas.helpers.ViewAwareSubscription

class WidgetDelegate<ModelT: Model, WidgetT>(
    private val widget: WidgetT
) where WidgetT: Widget<ModelT>, WidgetT: View {

    private val appStateObservable = widget.context.appComponent.appStateObservable

    private var currentModel: ModelT? by observable { oldValue, newValue ->
        if (newValue != null && oldValue != newValue) {
            widget.update(newValue)
        }
    }

    init {
        ViewAwareSubscription(widget, appStateObservable) {
            currentModel = widget.adapter.toModel(it)
        }
    }
}