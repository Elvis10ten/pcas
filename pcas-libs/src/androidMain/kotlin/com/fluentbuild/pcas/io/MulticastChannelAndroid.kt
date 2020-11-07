package com.fluentbuild.pcas.io

import android.content.Context
import android.net.wifi.WifiManager
import com.fluentbuild.pcas.io.transport.MessageReceiver
import com.fluentbuild.pcas.utils.wifiManager
import com.fluentbuild.pcas.values.Provider
import java.net.MulticastSocket

internal class MulticastChannelAndroid(
    private val context: Context,
    hostAddressProvider: Provider<Address.Ipv4>,
    socketWrapper: SocketWrapper<MulticastSocket>
): MulticastChannelJvm(hostAddressProvider, socketWrapper) {

    private var multicastLock: WifiManager.MulticastLock? = null

    override fun init(receiver: MessageReceiver) {
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

        private const val TAG_MULTICAST_LOCK = "PCAS_MULTICAST_LOCK"
    }
}