package com.fluentbuild.pcas.ui

import androidx.cardview.widget.CardView
import androidx.core.text.parseAsHtml
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.logs.RichLog
import com.fluentbuild.pcas.utils.isScrolledToBottom
import com.fluentbuild.pcas.utils.scrollToBottom
import kotlinx.android.synthetic.main.activity_main.view.*

class ConsoleRenderer(consoleCard: CardView) {

    private val textView = consoleCard.consoleTextView
    private val scrollView = consoleCard.consoleScrollView

    fun render(): Cancellable {
        textView.text = RichLog.getLines().parseAsHtml()
        scrollView.scrollToBottom()

        RichLog.observer = { line ->
            val isScrolledToBottom = scrollView.isScrolledToBottom()
            textView.append(line.parseAsHtml())
            if(isScrolledToBottom) {
                scrollView.scrollToBottom()
            }
        }


        return object: Cancellable {

            override fun cancel() {
                RichLog.observer = null
            }
        }
    }

    fun clearConsole() {
        RichLog.clear()
        textView.text = ""
    }
}