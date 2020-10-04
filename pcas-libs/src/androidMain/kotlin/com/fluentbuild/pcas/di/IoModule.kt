package com.fluentbuild.pcas.di

import android.content.Context
import com.fluentbuild.pcas.async.ThreadRunner
import com.fluentbuild.pcas.io.*
import java.net.DatagramSocket
import java.security.SecureRandom
import javax.crypto.SecretKey

class IoModule internal constructor(
    private val appContext: Context,
    private val networkKey: SecretKey,
    private val secureRandom: SecureRandom,
    private val threadRunnerProvider: () -> ThreadRunner
) {

    private val parceler: Parceler by lazy { Parceler(networkKey, secureRandom, BufferObjectPool) }

    internal val multicastChannel: MulticastChannel by lazy { AndroidMulticastChannel(appContext, getSocketWrapper()) }

    internal val unicastChannel: UnicastChannel by lazy { SecuredUnicastChannel(getSocketWrapper()) }

    private fun <SocketT: DatagramSocket> getSocketWrapper() =
        SocketWrapper<SocketT>(parceler, BufferObjectPool, threadRunnerProvider())
}