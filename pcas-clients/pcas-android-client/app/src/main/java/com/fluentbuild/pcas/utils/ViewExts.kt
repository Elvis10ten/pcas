package com.fluentbuild.pcas.utils

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt

inline val String.asColorStateList get() = ColorStateList.valueOf(this.toColorInt())

inline fun <reified ViewT: View> Context.inflate(@LayoutRes layout: Int, root: ViewGroup?) =
    LayoutInflater.from(this).inflate(layout, root, false) as ViewT

inline fun <reified ViewT: View> ViewGroup.addLayout(@LayoutRes layout: Int) =
    context.inflate<ViewT>(layout, this).also { addView(it) }

fun Context.createColorStateList(@ColorRes id: Int) = ColorStateList.valueOf(getColorCompat(id))

fun Context.getColorCompat(@ColorRes id: Int) = ContextCompat.getColor(this, id)

fun Context.getDrawableCompat(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)!!

fun View.setVisible(visible: Boolean) {
    visibility = if(visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}