package com.fluentbuild.pcas.actions

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.fluentbuild.pcas.widgets.EncryptionSetupFragment

object EncryptionShowSetupAction: Action {

    override fun perform(context: Context) {
        EncryptionSetupFragment.newInstance().show(
            (context as AppCompatActivity).supportFragmentManager,
            EncryptionSetupFragment::class.simpleName
        )
    }
}