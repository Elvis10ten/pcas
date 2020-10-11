package com.fluentbuild.pcas.di

import android.content.Context
import android.os.Handler
import com.fluentbuild.pcas.AndroidHostInfoObservable
import com.fluentbuild.pcas.Uuid
import com.fluentbuild.pcas.async.ThreadRunner
import com.fluentbuild.pcas.io.*
import com.fluentbuild.pcas.services.audio.AudioConfig
import java.net.DatagramSocket
import java.security.SecureRandom
import javax.crypto.SecretKey

internal class IoModule(
    appContext: Context,
    mainHandler: Handler,
    hostUuid: Uuid,
    hostName: String,
    networkKey: SecretKey,
    private val threadRunner: () -> ThreadRunner,
    secureRandom: SecureRandom
) {

    private val addressProvider = AndroidAddressProvider(appContext)

    private val parceler = Parceler(networkKey, secureRandom, BufferObjectPool)

    internal val unicastChannel = JvmUnicastChannel(getSocketWrapper())

    internal val selfHostObservable = AndroidHostInfoObservable(
        context = appContext,
        mainHandler = mainHandler,
        hostUuid = hostUuid,
        hostName = hostName,
        addressProvider = addressProvider,
        unicastChannel = unicastChannel,
        audioConfig = AudioConfig
    )

    internal val multicastChannel = AndroidMulticastChannel(appContext, selfHostObservable, getSocketWrapper())


    private fun <SocketT: DatagramSocket> getSocketWrapper() =
        SocketWrapper<SocketT>(parceler, BufferObjectPool, threadRunner())
}