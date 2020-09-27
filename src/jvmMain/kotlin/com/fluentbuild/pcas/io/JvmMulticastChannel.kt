package com.fluentbuild.pcas.io

import com.fluentbuild.pcas.async.ThreadExecutor
import com.fluentbuild.pcas.utils.logger
import java.lang.Exception
import java.net.MulticastSocket

open class JvmMulticastChannel(
    private val cipher: Cipher,
    private val executor: ThreadExecutor
): MulticastChannel {

    private val log by logger()
    private val receiveBuffer = ByteArray(MAX_PACKET_SIZE)

    @Volatile
    private var socket: MulticastSocket? = null

    override fun init(receiver: PayloadReceiver) {
        require(socket == null) { "MulticastChannel already initialized" }
        socket = MulticastSocket(GROUP_PORT).apply {
            timeToLive = MULTICAST_TTL
            trafficClass = IP_TOS_THROUGHPUT
            joinGroup(GROUP_ADDRESS.inetAddress)
        }

        executor.onBackground {
            while(socket != null) {
                try {
                    val decryptedPayload = socket!!.awaitPayload(receiveBuffer, cipher)
                    executor.onMain { receiver.onReceived(decryptedPayload) }
                } catch (e: Exception) {
                    log.error(e) { "Error receiving payload" }
                }
            }
        }
    }

    override fun broadcast(payload: ByteArray) {
        log.debug { "Broadcasting payload" }
        executor.onBackground {
            val encryptedPayload = cipher.encrypt(payload)
            val packet = createDatagramPacket(encryptedPayload, GROUP_ADDRESS, GROUP_PORT)
            socket!!.send(packet)
        }
    }

    override fun close() {
        log.debug { "Closing MulticastChannel" }
        executor.cancel()
        socket?.leaveGroup(GROUP_ADDRESS.inetAddress)
        socket?.close()
        socket = null
    }

    companion object {

        private const val MULTICAST_TTL = 255
        private const val GROUP_PORT = 49137
        private val GROUP_ADDRESS = Address.Ipv4("225.139.089.176")
    }
}