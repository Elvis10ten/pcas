package com.fluentbuild.pcas.actions

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.fluentbuild.pcas.widgets.PeripheralListFragment
import com.fluentbuild.pcas.services.ServiceClass

class ShowPeripheralListAction(private val serviceClass: ServiceClass): Action {

    override fun perform(context: Context) {
        PeripheralListFragment.newInstance(serviceClass).show(
            (context as AppCompatActivity).supportFragmentManager,
            PeripheralListFragment::class.simpleName
        )
    }
}