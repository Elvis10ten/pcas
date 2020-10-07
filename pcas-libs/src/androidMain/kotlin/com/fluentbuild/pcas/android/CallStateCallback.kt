package com.fluentbuild.pcas.android

import android.content.Context
import android.telephony.PhoneStateListener
import com.fluentbuild.pcas.logs.getLog

internal class CallStateCallback(
    private val context: Context,
    private val onChanged: () -> Unit
): PhoneStateListener() {

    private val log = getLog()

    override fun onCallStateChanged(state: Int, phoneNumber: String) {
        log.debug { "Call state changed: $state, phoneNumber: $phoneNumber" }
        onChanged()
    }

    fun register() {
        // TelephonyManager immediately invokes callback with current value
        context.telephonyManager.listen(this, LISTEN_CALL_STATE)
    }

    fun unregister() {
        context.telephonyManager.listen(this, LISTEN_NONE)
    }
}