package com.fluentbuild.pcas

import android.content.Context
import android.os.Handler
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.services.audio.AudioConfig
import com.fluentbuild.pcas.android.AddressChangeCallback
import com.fluentbuild.pcas.android.InteractivityCallback
import com.fluentbuild.pcas.android.powerManager
import com.fluentbuild.pcas.io.AndroidAddressProvider
import com.fluentbuild.pcas.io.SecureUnicastChannel
import com.fluentbuild.pcas.logs.getLog

internal class AndroidHostInfoObservable(
	private val context: Context,
	private val mainHandler: Handler,
	private val hostUuid: Uuid,
	private val hostName: String,
	private val addressProvider: AndroidAddressProvider,
	private val unicastChannel: SecureUnicastChannel,
	private val audioConfig: AudioConfig
): HostInfoObservable {

    private val log = getLog()

    override fun subscribe(observer: (HostInfo) -> Unit): Cancellable {
        log.debug { "Observing self HostInfo" }
        val notifyObserver = { observer(currentValue) }

        val activeNetworkCallback = AddressChangeCallback(context, mainHandler, addressProvider, notifyObserver)
        val interactivityCallback = InteractivityCallback(context, notifyObserver)

        notifyObserver()
        activeNetworkCallback.register(addressProvider.get())
        interactivityCallback.register()

        return Cancellable {
            log.debug { "Stopping self HostInfo observation" }
            activeNetworkCallback.unregister()
            interactivityCallback.unregister()
        }
    }

    override val currentValue: HostInfo
		get() = HostInfo(
        uuid = hostUuid,
        name = hostName,
        address = addressProvider.get(),
        port = unicastChannel.getPort(),
        isInteractive = context.powerManager.isInteractive,
        minBufferSizeBytes = audioConfig.minBufferSizeBytes
    )
}