package com.fluentbuild.pcas.console

import android.text.Html
import android.widget.TextView
import androidx.core.text.HtmlCompat

class ConsoleRenderer(
    private val provider: ConsoleItemProvider,
    private val textView: TextView
) {

    fun update() {
        val items = provider.get()
        val itemTexts = items.joinToString(
            separator = "<br/>",
            transform = { it.getRichText() }
        )
        textView.text = Html.fromHtml(itemTexts, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}