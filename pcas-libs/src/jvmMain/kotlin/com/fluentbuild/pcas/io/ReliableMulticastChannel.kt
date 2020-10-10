package com.fluentbuild.pcas.io

import com.fluentbuild.pcas.logs.getLog
import java.io.IOException
import java.net.Inet4Address
import java.net.InetSocketAddress
import java.net.MulticastSocket
import java.net.NetworkInterface

internal open class ReliableMulticastChannel(
    private val socketWrapper: SocketWrapper<MulticastSocket>
): MulticastChannel {

    private val log = getLog()

    @Throws(IOException::class)
    override fun init(receiver: MessageReceiver) {
        val f = NetworkInterface.getNetworkInterfaces()
            .asSequence()
            .first { boom ->
                boom.inetAddresses.asSequence().any { !it.isLoopbackAddress && it is Inet4Address }
            }
        log.error { "hey: $f" }
        socketWrapper.init(MulticastSocket(MULTICAST_PORT)) {
            trafficClass = IpTos.RELIABILITY.value
            loopbackMode = true
            timeToLive = MULTICAST_TTL
            networkInterface = f
            joinGroup(InetSocketAddress(MULTICAST_ADDRESS.inetAddress, MULTICAST_PORT), f)
        }

        socketWrapper.startReceiving(receiver)
    }

    @Throws(IOException::class)
    override fun broadcast(message: ByteArray, messageSize: Int) {
        socketWrapper.send(message, messageSize, MULTICAST_ADDRESS, MULTICAST_PORT)
    }

    override fun close() {
        socketWrapper.close {
            try {
                leaveGroup(MULTICAST_ADDRESS.inetAddress)
            } catch (e: IOException) {
                log.error(e) { "Failed to leave multicast group" }
            }
        }
    }
}