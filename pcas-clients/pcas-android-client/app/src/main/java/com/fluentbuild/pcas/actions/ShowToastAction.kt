package com.fluentbuild.pcas.actions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

class ShowToastAction(@StringRes private val message: Int): Action {

    override fun perform(context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}