package com.fluentbuild.pcas.android

import android.content.Context
import android.net.*
import android.os.Handler
import com.fluentbuild.pcas.io.AndroidAddressProvider
import com.fluentbuild.pcas.io.Address
import com.fluentbuild.pcas.logs.getLog

internal class AddressChangeCallback(
	private val context: Context,
	private val mainHandler: Handler,
	private val addressProvider: AndroidAddressProvider,
	private val onChanged: () -> Unit
): ConnectivityManager.NetworkCallback() {

    private val log = getLog()
    private lateinit var lastAddress: Address.Ipv4

    private val networkRequest: NetworkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        .build()

    override fun onAvailable(network: Network) {
        log.debug(::onAvailable, network)
        checkAddress()
    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        log.debug(::onLinkPropertiesChanged, network, linkProperties)
        checkAddress()
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        log.debug(::onCapabilitiesChanged, network, networkCapabilities)
        checkAddress()
    }

    fun register(currentAddress: Address.Ipv4) {
        lastAddress = currentAddress
        context.connectivityManager.registerNetworkCallback(networkRequest, this, mainHandler)
    }

    fun unregister() {
        context.connectivityManager.unregisterNetworkCallback(this)
    }

    private fun checkAddress() {
        if(addressProvider.get() != lastAddress) {
            onChanged()
        }
    }
}