package com.fluentbuild.pcas.actions

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.fluentbuild.pcas.SetupFragment

object SetupSecurityAction: Action {

    override fun perform(context: Context) {
        SetupFragment().show(
            (context as AppCompatActivity).supportFragmentManager,
            SetupFragment::class.simpleName
        )
    }
}