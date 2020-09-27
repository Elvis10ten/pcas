package com.fluentbuild.pcas.io

import android.content.Context
import android.net.wifi.WifiManager
import com.fluentbuild.pcas.android.wifiManager
import com.fluentbuild.pcas.async.ThreadExecutor

class AndroidMulticastChannel(
    private val context: Context,
    cipher: Cipher,
    executor: ThreadExecutor
): JvmMulticastChannel(cipher, executor) {

    private var multicastLock: WifiManager.MulticastLock? = null

    override fun init(receiver: PayloadReceiver) {
        multicastLock = context.wifiManager.createMulticastLock(TAG_MULTICAST_LOCK).apply {
            setReferenceCounted(false)
            acquire()
        }
        super.init(receiver)
    }

    override fun close() {
        multicastLock?.release()
        multicastLock = null
        super.close()
    }

    companion object {

        private const val TAG_MULTICAST_LOCK = "TAG_MULTICAST_LOCK"
    }
}