package com.fluentbuild.pcas.actions

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.fluentbuild.pcas.PeripheralListFragment
import com.fluentbuild.pcas.SetupFragment
import com.fluentbuild.pcas.services.ServiceClass

object SetupSecurityAction: Action {

    override fun perform(context: Context) {
        SetupFragment().show(
            (context as AppCompatActivity).supportFragmentManager,
            SetupFragment::class.simpleName
        )
    }
}