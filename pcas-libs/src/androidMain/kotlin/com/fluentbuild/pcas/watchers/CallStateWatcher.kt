package com.fluentbuild.pcas.watchers

import android.content.Context
import android.telephony.PhoneStateListener
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.utils.telephonyManager

internal class CallStateWatcher(private val context: Context): Watcher<Int>() {

    private val log = getLog()
    private val phoneStateListener = object: PhoneStateListener() {

        override fun onCallStateChanged(state: Int, phoneNumber: String) {
            log.debug(::onCallStateChanged, state, phoneNumber)
            onUpdated(state)
        }
    }

    override fun registerInternal() {
        // TelephonyManager immediately invokes callback with current value
        context.telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    override fun unregisterInternal() {
        context.telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
    }
}