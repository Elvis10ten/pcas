package com.fluentbuild.pcas.di

import android.content.Context
import android.os.Handler
import com.fluentbuild.pcas.io.AndroidAddressProvider
import com.fluentbuild.pcas.AndroidHostInfoObservable
import com.fluentbuild.pcas.io.SecureUnicastChannel
import com.fluentbuild.pcas.Uuid
import com.fluentbuild.pcas.services.audio.AudioConfig

internal class HostModule(
    appContext: Context,
    mainHandler: Handler,
    hostUuid: Uuid,
    hostName: String,
    unicastChannel: SecureUnicastChannel
) {

    private val addressProvider = AndroidAddressProvider(appContext)

    internal val selfHostInfoWatcher = AndroidHostInfoObservable(
        context = appContext,
        mainHandler = mainHandler,
        hostUuid = hostUuid,
        hostName = hostName,
        addressProvider = addressProvider,
        unicastChannel = unicastChannel,
        audioConfig = AudioConfig
    )
}