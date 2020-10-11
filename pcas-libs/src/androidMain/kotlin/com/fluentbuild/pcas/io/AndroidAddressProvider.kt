package com.fluentbuild.pcas.io

import android.content.Context
import com.fluentbuild.pcas.android.wifiManager
import com.fluentbuild.pcas.values.Provider
import java.net.InetAddress

internal class AndroidAddressProvider(private val context: Context): Provider<Address.Ipv4> {

    override fun get() = Address.Ipv4(getInetAddress().hostAddress)

    private fun getInetAddress() = getWifiAddress() ?: getPrimaryNetworkInterfaceAddress()

    private fun getWifiAddress(): InetAddress? {
        return context.wifiManager.connectionInfo.ipAddress
            .takeIf { it != 0 }
            ?.let {
                byteArrayOf(
                    (0xff and it).toByte(),
                    (0xff and (it shr 8)).toByte(),
                    (0xff and (it shr 16)).toByte(),
                    (0xff and (it shr 24)).toByte()
                )
            }
            ?.let { InetAddress.getByAddress(it) }
    }
}