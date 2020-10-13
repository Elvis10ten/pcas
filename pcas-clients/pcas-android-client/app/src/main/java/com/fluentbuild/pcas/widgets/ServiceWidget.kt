package com.fluentbuild.pcas.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.fluentbuild.pcas.R
import com.fluentbuild.pcas.actions.SelectPeripheralAction
import com.fluentbuild.pcas.adapters.ServiceClassAdapter
import com.fluentbuild.pcas.models.ServiceClassModel
import com.fluentbuild.pcas.services.ServiceClass
import com.fluentbuild.pcas.utils.addLayout
import kotlinx.android.synthetic.main.item_service.view.*

@SuppressLint("ViewConstructor")
class ServiceWidget(
    context: Context,
    private val serviceClass: ServiceClass,
): FrameLayout(context), Widget<ServiceClassModel> {

    private val itemView: View = addLayout(R.layout.item_service)

    init {
        WidgetDelegate(this)
    }

    override val adapter = ServiceClassAdapter(serviceClass)

    override fun update(model: ServiceClassModel) {
        serviceIconView.setImageResource(model.icon)
        serviceIconView.imageTintList = ColorStateList.valueOf(model.iconColor)
        serviceIconView.backgroundTintList = ColorStateList.valueOf(model.iconBackgroundColor)

        val badgeIcon = when(model.connectionCount) {
            0 -> R.drawable.ic_clear
            1 -> R.drawable.ic_check
            2 -> R.drawable.ic_check_double
            else -> R.drawable.ic_more_horiz
        }
        serviceIconBadgeView.setImageResource(badgeIcon)

        serviceNameView.setText(model.name)

        itemView.setOnClickListener {
            if(model.isEnabled) {
                SelectPeripheralAction(serviceClass).perform(context)
            } else {
                Toast.makeText(context, R.string.serviceUnsupportedError, Toast.LENGTH_LONG).show()
            }
        }
    }
}