package com.fluentbuild.pcas.console

import android.text.Html
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.fluentbuild.pcas.logs.ConsoleProvider

class ConsoleRenderer(
    private val textView: TextView
) {

    fun update() {
        val items = ConsoleProvider.messages
        val itemTexts = items.joinToString(
            separator = "<br/><br/>",
            transform = { it }
        )
        textView.text = Html.fromHtml(itemTexts, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}