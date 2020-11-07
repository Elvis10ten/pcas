package com.fluentbuild.pcas.di

import android.content.Context
import com.fluentbuild.pcas.host.HostConfig
import com.fluentbuild.pcas.host.HostInfoObservableAndroid
import com.fluentbuild.pcas.io.Address
import com.fluentbuild.pcas.io.transport.UnicastChannel
import com.fluentbuild.pcas.services.audio.AudioConfig
import com.fluentbuild.pcas.values.Provider
import com.fluentbuild.pcas.watchers.InteractivityWatcher
import com.fluentbuild.pcas.watchers.NetworkAddressWatcher

internal class HostModule(
	appContext: Context,
	hostConfig: HostConfig,
	unicastChannel: UnicastChannel,
	networkAddressWatcher: NetworkAddressWatcher,
	interactivityWatcher: InteractivityWatcher,
	hostAddressProvider: Provider<Address.Ipv4>
) {

	val hostObservable = HostInfoObservableAndroid(
		context = appContext,
		hostUuid = hostConfig.uuid,
		hostName = hostConfig.name,
		hostAddressProvider = hostAddressProvider,
		unicastChannel = unicastChannel,
		audioConfig = AudioConfig,
		networkAddressWatcher = networkAddressWatcher,
		interactivityWatcher = interactivityWatcher
	)
}