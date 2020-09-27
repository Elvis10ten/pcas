package com.fluentbuild.pcas.host

import android.content.Context
import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.services.audio.AudioConfig
import com.fluentbuild.pcas.android.ActiveNetworkCallback
import com.fluentbuild.pcas.android.InteractivityReceiver
import com.fluentbuild.pcas.android.powerManager
import com.fluentbuild.pcas.io.UnicastChannel
import com.fluentbuild.pcas.utils.logger

class AndroidHostInfoWatcher(
    private val context: Context,
    private val hostUuid: String,
    private val hostName: String,
    private val addressProvider: NetworkAddressProvider,
    private val unicastChannel: UnicastChannel
): HostInfoWatcher {

    private val log by logger()

    override fun watch(consumer: (HostInfo) -> Unit): Cancellable {
        log.debug { "Watching HostInfo" }
        val notifyConsumer = {
            getCurrentHostInfo().run {
                log.debug { "Current HostInfo: $this" }
                consumer(this)
            }
        }

        val activeNetworkCallback = ActiveNetworkCallback(context, notifyConsumer)
        val interactivityReceiver = InteractivityReceiver(context, notifyConsumer)

        notifyConsumer()
        activeNetworkCallback.register()
        interactivityReceiver.register()

        return Cancellable {
            log.debug { "Stopping HostInfo watch" }
            activeNetworkCallback.unregister()
            interactivityReceiver.unregister()
        }
    }

    private fun getCurrentHostInfo(): HostInfo {
        return HostInfo(
            uuid = hostUuid,
            name = hostName,
            ip = addressProvider.getHostAddress(),
            port = unicastChannel.getPort(),
            isInteractive = context.powerManager.isInteractive,
            minBufferSizeBytes = AudioConfig.minBufferSizeBytes
        )
    }
}