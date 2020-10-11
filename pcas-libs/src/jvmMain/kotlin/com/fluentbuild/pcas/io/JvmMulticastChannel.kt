package com.fluentbuild.pcas.io

import com.fluentbuild.pcas.HostInfoObservable
import com.fluentbuild.pcas.logs.getLog
import java.io.IOException
import java.net.*

internal open class JvmMulticastChannel(
    private val hostObservable: HostInfoObservable,
    private val socketWrapper: SocketWrapper<MulticastSocket>
): SecureMulticastChannel {

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
                val hostInterface = hostObservable.currentValue.address.getPrimaryInterfaceWithAddress()
                setNetworkInterface(hostInterface)
                joinGroup(InetSocketAddress(MULTICAST_ADDRESS.inetAddress, MULTICAST_PORT), hostInterface)
            }
        }

        socketWrapper.startReceiving(receiver)
    }

    @Throws(IOException::class)
    override fun send(message: ByteArray, messageSize: Int) {
        socketWrapper.send(message, messageSize, MULTICAST_ADDRESS, MULTICAST_PORT)
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