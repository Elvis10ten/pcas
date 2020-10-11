package com.fluentbuild.pcas.di

import android.content.Context
import android.os.Handler
import com.fluentbuild.pcas.io.Address
import com.fluentbuild.pcas.values.Provider
import com.fluentbuild.pcas.watchers.InteractivityWatcher
import com.fluentbuild.pcas.watchers.NetworkAddressWatcher

internal class WatchersModule(
	appContext: Context,
	mainThreadHandler: Handler,
	hostAddressProvider: Provider<Address.Ipv4>
) {

	val networkAddressWatcher = NetworkAddressWatcher(appContext, mainThreadHandler, hostAddressProvider)

	val interactivityWatcher = InteractivityWatcher(appContext)
}