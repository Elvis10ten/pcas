package com.fluentbuild.pcas.host

import android.content.Context
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.services.audio.AudioConfig
import com.fluentbuild.pcas.android.ActiveNetworkCallback
import com.fluentbuild.pcas.android.InteractivityCallback
import com.fluentbuild.pcas.android.powerManager
import com.fluentbuild.pcas.async.Watcher
import com.fluentbuild.pcas.io.UnicastChannel
import com.fluentbuild.pcas.utils.logger

class AndroidHostInfoWatcher(
    private val context: Context,
    private val hostUuid: String,
    private val hostName: String,
    private val addressProvider: HostAddressProvider,
    private val unicastChannel: UnicastChannel
): HostInfoWatcher {

    private val log by logger()

    override fun watch(consumer: (HostInfo) -> Unit): Cancellable {
        log.debug { "Watching HostInfo" }
        val notifyConsumer = {
            getCurrentHostInfo().let {
                log.debug { "Current HostInfo: $it" }
                consumer(it)
            }
        }

        val activeNetworkCallback = ActiveNetworkCallback(context, notifyConsumer)
        val interactivityCallback = InteractivityCallback(context, notifyConsumer)

        notifyConsumer()
        activeNetworkCallback.register()
        interactivityCallback.register()

        return Cancellable {
            log.debug { "Stopping HostInfo watch" }
            activeNetworkCallback.unregister()
            interactivityCallback.unregister()
        }
    }

    override val currentValue: HostInfo
        get() = getCurrentHostInfo()

    private fun getCurrentHostInfo(): HostInfo {
        return HostInfo(
            uuid = hostUuid,
            name = hostName,
            address = addressProvider.getAddress(),
            port = unicastChannel.getPort(),
            isInteractive = context.powerManager.isInteractive,
            minBufferSizeBytes = AudioConfig.minBufferSizeBytes
        )
    }
}