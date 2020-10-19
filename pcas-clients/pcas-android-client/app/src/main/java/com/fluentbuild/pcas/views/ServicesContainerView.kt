package com.fluentbuild.pcas.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.fluentbuild.pcas.services.ServiceClass
import com.fluentbuild.pcas.widgets.ServiceWidget

class ServicesContainerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): LinearLayout(context, attrs, defStyle) {

    init {
        orientation = VERTICAL
        ServiceClass.values().forEach {
            addView(ServiceWidget(context, it), getServiceWidgetParams())
        }
    }

    private fun getServiceWidgetParams() =
        LayoutParams(LayoutParams.MATCH_PARENT, 0).apply {
            weight = 1f
        }
}