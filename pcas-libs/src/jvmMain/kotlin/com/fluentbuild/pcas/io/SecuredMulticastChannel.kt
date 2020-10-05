package com.fluentbuild.pcas.io

import com.fluentbuild.pcas.logs.logger
import java.io.IOException
import java.net.MulticastSocket

internal open class SecuredMulticastChannel(
    private val socketWrapper: SocketWrapper<MulticastSocket>
): MulticastChannel {

    private val log by logger()

    @Throws(IOException::class)
    override fun init(receiver: MessageReceiver) {
        socketWrapper.init(MulticastSocket(MULTICAST_PORT)) {
            loopbackMode = true
            timeToLive = MULTICAST_TTL
            joinGroup(MULTICAST_ADDRESS.inetAddress)
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