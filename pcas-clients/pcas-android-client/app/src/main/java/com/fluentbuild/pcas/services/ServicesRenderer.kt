package com.fluentbuild.pcas.services

import android.content.res.ColorStateList
import android.view.View
import android.widget.LinearLayout
import com.fluentbuild.pcas.R
import com.fluentbuild.pcas.inflateInto
import kotlinx.android.synthetic.main.item_service.view.*

internal class ServicesRenderer(
    private val provider: ServicesProvider,
    private val container: LinearLayout,
    private val onClick: (ServiceUiModel) -> Unit
) {

    fun update() {
        val services = provider.get()
        if(container.childCount != services.size) {
            container.removeAllViews()
            repeat(services.size) { container.inflateInto<View>(R.layout.item_service) }
        }

        services.forEachIndexed { index, service ->
            service.bind(container.getChildAt(index))
        }
    }

    private fun ServiceUiModel.bind(view: View) {
        view.serviceIconView.setImageResource(icon)
        view.serviceIconView.imageTintList = ColorStateList.valueOf(iconColor)
        view.serviceIconView.backgroundTintList = ColorStateList.valueOf(iconBackgroundColor)

        view.serviceNameView.setText(name)
        view.setOnClickListener { onClick(this) }

        val badgeIcon = when(state) {
            ServiceUiModel.BondState.HAS_A_BOND -> R.drawable.ic_check
            ServiceUiModel.BondState.IN_PROGRESS -> R.drawable.ic_more_horiz
            ServiceUiModel.BondState.NO_BONDS -> R.drawable.ic_clear
        }
        view.serviceIconBadgeView.setImageResource(badgeIcon)
    }
}