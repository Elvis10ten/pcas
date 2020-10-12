package com.fluentbuild.pcas.services

import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.fluentbuild.pcas.R
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.utils.inflate
import kotlinx.android.synthetic.main.item_service.view.*

internal class ServicesRenderer(
    private val container: LinearLayout,
    private val onClick: (ServiceUiModel) -> Unit
) {

    private lateinit var audioServiceView: View
    private lateinit var mouseServiceView: View
    private lateinit var keypadServiceView: View
    private lateinit var healthServiceView: View

    fun init() {
        audioServiceView = container.inflateInto(R.layout.item_service)
        mouseServiceView = container.inflateInto(R.layout.item_service)
        keypadServiceView = container.inflateInto(R.layout.item_service)
        healthServiceView = container.inflateInto(R.layout.item_service)
    }

    fun render(): Cancellable {
        provider.getAudioService().bind(audioServiceView)
        provider.getMouseService().bind(mouseServiceView)
        provider.getKeypadService().bind(keypadServiceView)
        provider.getHealthService().bind(healthServiceView)

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

fun <T: View> ViewGroup.inflateInto(@LayoutRes layoutRes: Int) =
    context.inflate<T>(layoutRes, this).also { addView(it) }