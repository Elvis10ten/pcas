package com.fluentbuild.pcas.actions

import android.content.Context
import com.fluentbuild.pcas.MainService
import com.fluentbuild.pcas.R
import com.fluentbuild.pcas.appComponent
import com.fluentbuild.pcas.bluetoothAdapter
import com.fluentbuild.pcas.services.ServiceClass
import com.fluentbuild.pcas.utils.VersionUtils

object EngineManualStartAction: Action {

    override fun perform(context: Context) {
        if(!context.bluetoothAdapter.isEnabled) {
            ToastShowAction(context.getString(R.string.bluetoothDisabledError)).perform(context)
        } else if(!context.appComponent.hostConfigStore.get().hasAnyPeripheral) {
            PeripheralShowListAction(ServiceClass.AUDIO).perform(context)
        } else if(VersionUtils.isAtLeastAndroidTen() && context.appComponent.mediaProjection == null) {
            MainService.dryStart(context)
            MediaProjectionRequestAction.perform(context)
        } else {
            EngineStartAction.perform(context)
        }
    }
}