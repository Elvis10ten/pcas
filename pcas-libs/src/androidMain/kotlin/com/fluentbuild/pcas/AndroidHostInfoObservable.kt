package com.fluentbuild.pcas

import android.content.Context
import android.os.Handler
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.services.audio.AudioConfig
import com.fluentbuild.pcas.android.AddressChangeCallback
import com.fluentbuild.pcas.android.InteractivityCallback
import com.fluentbuild.pcas.android.powerManager
import com.fluentbuild.pcas.io.Address
import com.fluentbuild.pcas.io.SecureUnicastChannel
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.values.Provider

internal class AndroidHostInfoObservable(
	private val context: Context,
	private val mainHandler: Handler,
	private val hostConfig: HostConfig,
	private val hostAddressProvider: Provider<Address.Ipv4>,
	private val unicastChannel: SecureUnicastChannel,
	private val audioConfig: AudioConfig
): HostInfoObservable {

    private val log = getLog()

    override fun subscribe(observer: (HostInfo) -> Unit): Cancellable {
        log.debug { "Observing self HostInfo" }
        val notifyObserver = { observer(currentValue) }

        val activeNetworkCallback = AddressChangeCallback(context, mainHandler, hostAddressProvider, notifyObserver)
        val interactivityCallback = InteractivityCallback(context, notifyObserver)

        notifyObserver()
        activeNetworkCallback.register(hostAddressProvider.get())
        interactivityCallback.register()

        return Cancellable {
            log.debug { "Stopping self HostInfo observation" }
            activeNetworkCallback.unregister()
            interactivityCallback.unregister()
        }
    }

	override val currentValue: HostInfo
		get() = HostInfo(
			uuid = hostConfig.uuid,
			name = hostConfig.name,
			address = hostAddressProvider.get(),
			port = unicastChannel.getPort(),
			isInteractive = context.powerManager.isInteractive,
			minBufferSizeBytes = audioConfig.minBufferSizeBytes
		)
}