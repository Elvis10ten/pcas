package com.fluentbuild.pcas.actions

import android.content.Context
import android.widget.Toast

class ToastShowAction(private val message: String): Action {

    override fun perform(context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}