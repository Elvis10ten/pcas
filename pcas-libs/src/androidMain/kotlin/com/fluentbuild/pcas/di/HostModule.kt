package com.fluentbuild.pcas.di

import android.content.Context
import com.fluentbuild.pcas.host.*

internal class HostModule(
    private val appContext: Context,
    private val hostUuid: String,
    private val hostName: String,
    private val ioModule: IoModule
) {

    private val addressProvider: HostAddressProvider by lazy {
        NetworkAddressProvider(appContext)
    }

    internal val selfHostInfoWatcher: HostInfoObservable by lazy {
        AndroidHostInfoObservable(
            context = appContext,
            hostUuid = hostUuid,
            hostName = hostName,
            addressProvider = addressProvider,
            unicastChannel = ioModule.unicastChannel
        )
    }
}