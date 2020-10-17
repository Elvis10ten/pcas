package com.fluentbuild.pcas.host

import android.content.Context
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.services.audio.AudioConfig
import com.fluentbuild.pcas.watchers.NetworkAddressWatcher
import com.fluentbuild.pcas.watchers.InteractivityWatcher
import com.fluentbuild.pcas.io.Address
import com.fluentbuild.pcas.io.UnicastChannel
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.utils.powerManager
import com.fluentbuild.pcas.values.Provider

internal class HostInfoObservableAndroid(
	private val context: Context,
	private val hostConfig: HostConfig,
	private val hostAddressProvider: Provider<Address.Ipv4>,
	private val unicastChannel: UnicastChannel,
	private val audioConfig: AudioConfig,
	private val networkAddressWatcher: NetworkAddressWatcher,
	private val interactivityWatcher: InteractivityWatcher
): HostInfoObservable {

    private val log = getLog()

    override fun subscribe(observer: (HostInfo) -> Unit): Cancellable {
        log.debug { "Observing HostInfo" }
		val initialHost = currentValue
		observer(initialHost)

        val notifyObserver = { observer(currentValue) }
		networkAddressWatcher.register(initialHost.address, notifyObserver)
		interactivityWatcher.register(initialHost.isInteractive, notifyObserver)

        return Cancellable {
            log.debug { "Stop observing HostInfo" }
			networkAddressWatcher.unregister()
			interactivityWatcher.unregister()
        }
    }

	override val currentValue: HostInfo
		get() = HostInfo(
			uuid = hostConfig.uuid,
			name = hostConfig.name,
			address = hostAddressProvider.get(),
			port = unicastChannel.getPort(),
			isInteractive = context.powerManager.isInteractive,
			minAudioBufferSizeBytes = audioConfig.minBufferSizeBytes
		)
}