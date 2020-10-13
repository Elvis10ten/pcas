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
        orientation = HORIZONTAL
        addView(ServiceWidget(context, ServiceClass.AUDIO))
        addView(ServiceWidget(context, ServiceClass.MOUSE))
        addView(ServiceWidget(context, ServiceClass.KEYPAD))
    }
}