package com.fluentbuild.pcas.actions

import android.content.Context
import com.fluentbuild.pcas.appComponent

class EncryptionSetKeyAction(private val networkKey: ByteArray?): Action {

    override fun perform(context: Context) {
        context.appComponent.hostConfigStore.setNetworkKey(networkKey)
        RestartEngineAction.perform(context)
    }
}