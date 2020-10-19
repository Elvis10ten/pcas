package com.fluentbuild.pcas.widgets.foundation

import android.content.Context
import android.view.View
import androidx.lifecycle.Lifecycle
import com.fluentbuild.pcas.appComponent
import com.fluentbuild.pcas.models.Model
import com.fluentbuild.pcas.utils.Delegates.observable

fun <ModelT: Model, WidgetT> WidgetT.init() where WidgetT: Widget<ModelT>, WidgetT: View {
    var currentModel: ModelT? by observable { oldValue, newValue ->
        if (newValue != null && oldValue != newValue) {
            update(newValue)
        }
    }

    ViewAwareSubscription(this, context.appComponent.engineStateObservable) { state ->
        currentModel = adapter.toModel(state)
    }
}

fun <ModelT: Model, WidgetT: Widget<ModelT>> WidgetT.init(context: Context, lifecycle: Lifecycle) {
    var currentModel: ModelT? by observable { oldValue, newValue ->
        if (newValue != null && oldValue != newValue) {
            update(newValue)
        }
    }

    LifecycleAwareSubscription(lifecycle, context.appComponent.engineStateObservable) { state ->
        currentModel = adapter.toModel(state)
    }
}