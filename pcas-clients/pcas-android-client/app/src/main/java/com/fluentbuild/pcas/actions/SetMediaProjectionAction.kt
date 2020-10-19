package com.fluentbuild.pcas.actions

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.fluentbuild.pcas.appComponent
import com.fluentbuild.pcas.mediaProjectionManager

class SetMediaProjectionAction(
    private val resultCode: Int,
    private val data: Intent?
): Action {

    override fun perform(context: Context) {
        if(resultCode == Activity.RESULT_OK && data != null) {
            val projection = context.mediaProjectionManager.getMediaProjection(resultCode, data)
            context.appComponent.mediaProjection = projection
            context.appComponent.hostConfigStore.setAudioCaptureEnabled(true)
        } else {
            context.appComponent.hostConfigStore.setAudioCaptureEnabled(false)
        }

        StartEngineHardAction.perform(context)
    }
}