package com.fluentbuild.pcas.di

import android.content.Context
import com.fluentbuild.pcas.host.AndroidHostInfoWatcher
import com.fluentbuild.pcas.host.HostAddressProvider
import com.fluentbuild.pcas.host.HostInfoWatcher
import com.fluentbuild.pcas.host.NetworkAddressProvider

class HostModule(
    private val appContext: Context,
    private val hostUuid: String,
    private val hostName: String,
    private val ioModule: IoModule
) {

    private val addressProvider: HostAddressProvider by lazy {
        NetworkAddressProvider(appContext)
    }

    val selfHostInfoWatcher: HostInfoWatcher by lazy {
        AndroidHostInfoWatcher(
            context = appContext,
            hostUuid = hostUuid,
            hostName = hostName,
            addressProvider = addressProvider,
            unicastChannel = ioModule.unicastChannel
        )
    }
}