package com.fluentbuild.pcas.di

import android.content.Context
import com.fluentbuild.pcas.io.*
import javax.crypto.SecretKey

class IoModule(
    private val appContext: Context,
    private val payloadKey: SecretKey,
    private val asyncModule: AsyncModule
) {

    private val cipher: PayloadCipher by lazy {
        JvmPayloadCipher(payloadKey)
    }

    internal val multicastChannel: MulticastChannel by lazy {
        AndroidMulticastChannel(appContext, cipher, asyncModule.provideThreadExecutor())
    }

    internal val unicastChannel: UnicastChannel by lazy {
        JvmUnicastChannel(cipher, asyncModule.provideThreadExecutor())
    }
}