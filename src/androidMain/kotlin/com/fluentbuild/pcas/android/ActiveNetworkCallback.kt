package com.fluentbuild.pcas.android

import android.content.Context
import android.net.*
import com.fluentbuild.pcas.utils.logger

internal class ActiveNetworkCallback(
    private val context: Context,
    private val onChanged: () -> Unit
): ConnectivityManager.NetworkCallback() {

    private val log by logger()
    private val networkRequest: NetworkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        .build()

    override fun onAvailable(network: Network) {
        log.debug(::onAvailable, network)
        onChanged()
    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        log.debug(::onLinkPropertiesChanged, network, linkProperties)
        onChanged()
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        log.debug(::onCapabilitiesChanged, network, networkCapabilities)
        onChanged()
    }

    fun register() {
        context.connectivityManager.registerNetworkCallback(networkRequest, this)
    }

    fun unregister() {
        context.connectivityManager.unregisterNetworkCallback(this)
    }
}