package com.fluentbuild.pcas.di

import android.content.Context
import com.fluentbuild.pcas.async.ThreadRunner
import com.fluentbuild.pcas.io.*
import java.net.DatagramSocket
import java.security.SecureRandom
import javax.crypto.SecretKey

internal class IoModule(
    appContext: Context,
    networkKey: SecretKey,
    private val threadRunner: () -> ThreadRunner,
    secureRandom: SecureRandom
) {

    private val parceler = Parceler(networkKey, secureRandom, BufferObjectPool)

    internal val multicastChannel = AndroidMulticastChannel(appContext, getSocketWrapper())

    internal val unicastChannel = UnreliableUnicastChannel(getSocketWrapper())

    private fun <SocketT: DatagramSocket> getSocketWrapper() =
        SocketWrapper<SocketT>(parceler, BufferObjectPool, threadRunner())
}