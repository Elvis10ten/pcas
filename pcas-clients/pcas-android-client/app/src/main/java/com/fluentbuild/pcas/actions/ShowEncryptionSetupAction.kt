package com.fluentbuild.pcas.actions

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.fluentbuild.pcas.widgets.EncryptionSetupFragment

object ShowEncryptionSetupAction: Action {

    override fun perform(context: Context) {
        EncryptionSetupFragment().show(
            (context as AppCompatActivity).supportFragmentManager,
            EncryptionSetupFragment::class.simpleName
        )
    }
}