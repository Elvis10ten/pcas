package com.fluentbuild.pcas.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.text.parseAsHtml
import com.fluentbuild.pcas.R
import com.fluentbuild.pcas.adapters.ConsoleAdapter
import com.fluentbuild.pcas.models.ConsoleModel
import com.fluentbuild.pcas.utils.getColorCompat
import com.fluentbuild.pcas.utils.addLayout
import com.fluentbuild.pcas.utils.isScrolledToBottom
import com.fluentbuild.pcas.utils.scrollToBottom
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.console.view.*

class ConsoleWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): MaterialCardView(context, attrs, defStyle), Widget<ConsoleModel> {

    init {
        addLayout<View>(R.layout.console)
        setCardBackgroundColor(context.getColorCompat(R.color.colorConsoleBackground))
        WidgetDelegate(this)
    }

    override val adapter = ConsoleAdapter()

    override fun update(model: ConsoleModel) {
        val isScrolledToBottom = consoleScrollView.isScrolledToBottom()
        consoleTextView.text = model.lines.parseAsHtml()

        if(isScrolledToBottom) {
            consoleScrollView.scrollToBottom()
        }
    }
}