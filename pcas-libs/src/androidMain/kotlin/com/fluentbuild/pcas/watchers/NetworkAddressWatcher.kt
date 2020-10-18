package com.fluentbuild.pcas.watchers

import android.content.Context
import android.net.*
import android.os.Handler
import com.fluentbuild.pcas.utils.connectivityManager
import com.fluentbuild.pcas.io.Address
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.values.Provider

internal class NetworkAddressWatcher(
	private val context: Context,
	private val mainHandler: Handler,
	private val addressProvider: Provider<Address.Ipv4>
): Watcher<Address.Ipv4>() {

    private val log = getLog()
    private val connectivityManager get() = context.connectivityManager

    private val networkRequest: NetworkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        .build()

    private val networkCallback = object: ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            log.debug(::onAvailable, network)
            onUpdated(addressProvider.currentValue)
        }

        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
            log.debug(::onLinkPropertiesChanged, network, linkProperties)
            onUpdated(addressProvider.currentValue)
        }

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            log.debug(::onCapabilitiesChanged, network, networkCapabilities)
            onUpdated(addressProvider.currentValue)
        }
    }

    override fun registerInternal() {
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback, mainHandler)
    }

    override fun unregisterInternal() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}