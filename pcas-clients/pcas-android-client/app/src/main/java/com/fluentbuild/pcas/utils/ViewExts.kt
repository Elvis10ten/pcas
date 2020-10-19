package com.fluentbuild.pcas.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat

inline fun <reified ViewT: View> Context.inflate(@LayoutRes layout: Int, root: ViewGroup?) =
    LayoutInflater.from(this).inflate(layout, root, false) as ViewT

inline fun <reified ViewT: View> ViewGroup.addLayout(@LayoutRes layout: Int) =
    context.inflate<ViewT>(layout, this).also { addView(it) }

fun ViewGroup.isScrolledToBottom() =
    getChildAt(childCount - 1).bottom - (height + scrollY) <= 0

fun ScrollView.scrollToBottom() = post { fullScroll(View.FOCUS_DOWN) }

fun Context.createColorStateList(@ColorRes id: Int) = ColorStateList.valueOf(getColorCompat(id))

fun Context.createColorStateList(hex: String) = ColorStateList.valueOf(Color.parseColor(hex))

fun Context.getDrawableCompat(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)!!

fun Context.getColorCompat(@ColorRes id: Int) = ContextCompat.getColor(this, id)

fun View.setVisible(visible: Boolean) {
    visibility = if(visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}