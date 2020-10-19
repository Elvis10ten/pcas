package com.fluentbuild.pcas.widgets

import android.view.View
import com.fluentbuild.pcas.appComponent
import com.fluentbuild.pcas.models.Model
import com.fluentbuild.pcas.utils.Delegates.observable
import com.fluentbuild.pcas.helpers.ViewAwareSubscription

fun <ModelT: Model, WidgetT> WidgetT.initView() where WidgetT: Widget<ModelT>, WidgetT: View {
    var currentModel: ModelT? by observable { oldValue, newValue ->
        if (newValue != null && oldValue != newValue) {
            update(newValue)
        }
    }

    ViewAwareSubscription(this, context.appComponent.engineStateObservable) { state ->
        currentModel = adapter.toModel(state)
    }
}