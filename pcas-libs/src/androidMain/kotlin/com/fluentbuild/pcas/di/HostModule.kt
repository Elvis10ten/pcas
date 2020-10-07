package com.fluentbuild.pcas.di

import android.content.Context
import android.os.Handler
import com.fluentbuild.pcas.host.*
import com.fluentbuild.pcas.services.audio.AudioConfig

internal class HostModule(
    appContext: Context,
    mainHandler: Handler,
    hostUuid: Uuid,
    hostName: String,
    ioModule: IoModule
) {

    private val addressProvider = NetworkAddressProvider(appContext)

    internal val selfHostInfoWatcher = AndroidHostInfoObservable(
        context = appContext,
        mainHandler = mainHandler,
        hostUuid = hostUuid,
        hostName = hostName,
        addressProvider = addressProvider,
        unicastChannel = ioModule.unicastChannel,
        audioConfig = AudioConfig
    )
}