package com.fluentbuild.pcas.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.annotation.LayoutRes

internal fun ViewGroup.isScrolledToBottom() =
    getChildAt(childCount - 1).bottom - (height + scrollY) <= 0

internal fun ScrollView.scrollToBottom() = post { fullScroll(View.FOCUS_DOWN) }


@Suppress("UNCHECKED_CAST")
fun <T: View> Context.inflate(@LayoutRes layoutRes: Int, root: ViewGroup) =
    LayoutInflater.from(this).inflate(layoutRes, root, false) as T