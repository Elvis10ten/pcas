package com.fluentbuild.pcas.actions

import android.content.Context

fun interface Action {

    fun perform(context: Context)
}