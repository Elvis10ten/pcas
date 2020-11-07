package com.fluentbuild.pcas.io

import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.io.transport.*
import com.fluentbuild.pcas.io.transport.TransportConfig.MULTICAST_ADDRESS
import com.fluentbuild.pcas.io.transport.TransportConfig.MULTICAST_PORT
import com.fluentbuild.pcas.io.transport.TransportConfig.MULTICAST_TTL
import com.fluentbuild.pcas.values.Provider
import java.io.IOException
import java.net.InetSocketAddress
import java.net.MulticastSocket
import java.net.SocketException

internal open class MulticastChannelJvm(
    private val hostAddressProvider: Provider<Address.Ipv4>,
    private val socketWrapper: SocketWrapper<MulticastSocket>
): MulticastChannel {

    private val log = getLog()

    @Throws(IOException::class)
    override fun init(receiver: MessageReceiver) {
        socketWrapper.init(MulticastSocket(MULTICAST_PORT)) {
            trafficClass = IpTos.RELIABILITY.value
            loopbackMode = true
            timeToLive = MULTICAST_TTL

            try {
                joinGroup(MULTICAST_ADDRESS.inetAddress)
            } catch (e: SocketException) {
                log.error(e) { "Error joining group with default interface" }
                val hostInterface = hostAddressProvider.currentValue.getPrimaryInterfaceWithAddress()
                networkInterface = hostInterface
                joinGroup(InetSocketAddress(MULTICAST_ADDRESS.inetAddress, MULTICAST_PORT), hostInterface)
            }
        }

        socketWrapper.startReceiving(receiver)
    }

    @Throws(IOException::class)
    override fun send(parcel: ByteArray, messageSize: Int) {
        socketWrapper.send(parcel, messageSize, MULTICAST_ADDRESS, MULTICAST_PORT)
    }

    override fun close() {
        socketWrapper.close {
            try {
                leaveGroup(MULTICAST_ADDRESS.inetAddress)
            } catch (e: IOException) {
                log.error(e) { "Error leaving multicast group" }
            }
        }
    }
}