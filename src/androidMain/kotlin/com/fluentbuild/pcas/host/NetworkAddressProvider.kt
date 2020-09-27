package com.fluentbuild.pcas.host

import android.net.wifi.WifiManager
import com.fluentbuild.pcas.io.Address
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException

class NetworkAddressProvider(private val wifiManager: WifiManager): HostAddressProvider {

    override fun getHostAddress() = Address.Ipv4(getInetAddress().hostAddress)

    private fun getInetAddress() = getWifiAddress() ?: getPrimaryInterfaceAddress()

    private fun getWifiAddress(): InetAddress? {
        return wifiManager.connectionInfo.ipAddress
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

    @Throws(SocketException::class)
    private fun getPrimaryInterfaceAddress(): InetAddress {
        return NetworkInterface.getNetworkInterfaces()
            .asSequence()
            .map { it.inetAddresses.asSequence() }
            .flatten()
            .single { !it.isLoopbackAddress && it is Inet4Address }
    }
}