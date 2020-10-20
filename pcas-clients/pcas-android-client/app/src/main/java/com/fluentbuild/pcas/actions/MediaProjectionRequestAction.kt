package com.fluentbuild.pcas.actions

import android.app.Activity
import android.content.Context
import com.fluentbuild.pcas.mediaProjectionManager

object MediaProjectionRequestAction: Action {

    const val REQUEST_CODE = 1

    override fun perform(context: Context) {
        (context as Activity).startActivityForResult(
            context.mediaProjectionManager.createScreenCaptureIntent(),
            REQUEST_CODE
        )
    }
}