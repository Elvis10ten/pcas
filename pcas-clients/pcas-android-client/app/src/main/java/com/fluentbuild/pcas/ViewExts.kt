package com.fluentbuild.pcas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun <T: View> ViewGroup.inflateInto(@LayoutRes layoutRes: Int) =
    context.inflate<T>(layoutRes, this).also { addView(it) }

@Suppress("UNCHECKED_CAST")
fun <T: View> Context.inflate(@LayoutRes layoutRes: Int, root: ViewGroup) =
    LayoutInflater.from(this).inflate(layoutRes, root, false) as T