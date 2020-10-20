package com.fluentbuild.pcas.widgets.foundation

import android.content.Context
import android.view.View
import androidx.lifecycle.Lifecycle
import com.fluentbuild.pcas.appComponent
import com.fluentbuild.pcas.models.Model
import com.fluentbuild.pcas.utils.Delegates.observable

private fun <ModelT: Model, WidgetT: Widget<ModelT>> WidgetT.getOnChangeFunction(): (ModelT?, ModelT?) -> Unit {
    return { oldValue: ModelT?, newValue: ModelT? ->
        if (newValue != null && oldValue != newValue) {
            update(newValue)
        }
    }
}

fun <ModelT: Model, WidgetT> WidgetT.init() where WidgetT: Widget<ModelT>, WidgetT: View {
    var currentModel: ModelT? by observable(getOnChangeFunction())
    ViewAwareSubscription(this, context.appComponent.engineStateObservable) { state ->
        currentModel = adapter.buildModel(state)
    }
}

fun <ModelT: Model, WidgetT: Widget<ModelT>> WidgetT.init(context: Context, lifecycle: Lifecycle) {
    var currentModel: ModelT? by observable(getOnChangeFunction())
    LifecycleAwareSubscription(lifecycle, context.appComponent.engineStateObservable) { state ->
        currentModel = adapter.buildModel(state)
    }
}