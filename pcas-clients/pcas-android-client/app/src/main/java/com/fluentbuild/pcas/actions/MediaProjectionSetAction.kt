package com.fluentbuild.pcas.actions

import android.content.Context
import android.content.Intent
import com.fluentbuild.pcas.appComponent
import com.fluentbuild.pcas.mediaProjectionManager

class MediaProjectionSetAction(
    private val resultCode: Int,
    private val data: Intent?
): Action {

    override fun perform(context: Context) {
        if(data != null) {
            val projection = context.mediaProjectionManager.getMediaProjection(resultCode, data)
            context.appComponent.mediaProjection = projection
        }

        EngineStartAction.perform(context)
    }
}