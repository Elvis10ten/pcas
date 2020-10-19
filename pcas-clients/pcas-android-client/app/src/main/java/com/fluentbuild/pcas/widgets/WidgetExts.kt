package com.fluentbuild.pcas.widgets

import android.view.View
import androidx.fragment.app.Fragment
import com.fluentbuild.pcas.appComponent
import com.fluentbuild.pcas.models.Model
import com.fluentbuild.pcas.utils.Delegates.observable
import com.fluentbuild.pcas.utils.FragmentAwareSubscription
import com.fluentbuild.pcas.utils.ViewAwareSubscription

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

fun <ModelT: Model, WidgetT> WidgetT.initFragment() where WidgetT: Widget<ModelT>, WidgetT: Fragment {
    var currentModel: ModelT? by observable { oldValue, newValue ->
        if (newValue != null && oldValue != newValue) {
            update(newValue)
        }
    }

    FragmentAwareSubscription(this, requireContext().appComponent.engineStateObservable) { state ->
        currentModel = adapter.toModel(state)
    }
}