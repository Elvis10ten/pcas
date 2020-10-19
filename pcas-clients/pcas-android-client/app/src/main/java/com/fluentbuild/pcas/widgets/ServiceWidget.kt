package com.fluentbuild.pcas.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import com.fluentbuild.pcas.R
import com.fluentbuild.pcas.adapters.ServiceClassAdapter
import com.fluentbuild.pcas.models.ServiceClassModel
import com.fluentbuild.pcas.services.ServiceClass
import com.fluentbuild.pcas.widgets.views.addLayout
import com.fluentbuild.pcas.widgets.foundation.Widget
import com.fluentbuild.pcas.widgets.foundation.init
import kotlinx.android.synthetic.main.item_service.view.*


@SuppressLint("ViewConstructor")
class ServiceWidget(
    context: Context,
    serviceClass: ServiceClass,
): FrameLayout(context), Widget<ServiceClassModel> {

    init {
        TypedValue().let {
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, it, true)
            setBackgroundResource(it.resourceId)
        }

        addLayout<View>(R.layout.item_service)
        init()
    }

    override val adapter = ServiceClassAdapter(context, serviceClass)

    override fun update(model: ServiceClassModel) {
        serviceIconView.setImageDrawable(model.icon)
        serviceIconView.imageTintList = model.iconTintColor
        serviceIconView.backgroundTintList = model.iconBackgroundTintColor

        val badgeIcon = when(model.connectionCount) {
            0 -> R.drawable.ic_clear
            1 -> R.drawable.ic_check
            2 -> R.drawable.ic_check_double
            else -> R.drawable.ic_more_horiz
        }
        serviceIconBadgeView.setImageResource(badgeIcon)

        serviceNameTextView.text = model.name
        serviceDescTextView.text = model.description
        setOnClickListener { model.onClicked() }
    }
}