package com.fluentbuild.pcas.android

import android.content.Context
import android.telephony.PhoneStateListener

class CallStateCallback(
    private val context: Context,
    private val onChanged: () -> Unit
): PhoneStateListener() {

    override fun onCallStateChanged(state: Int, phoneNumber: String) {
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