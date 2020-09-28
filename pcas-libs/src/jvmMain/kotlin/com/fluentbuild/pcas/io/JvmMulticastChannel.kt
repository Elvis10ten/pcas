package com.fluentbuild.pcas.io

import com.fluentbuild.pcas.async.ThreadRunner
import com.fluentbuild.pcas.utils.logger
import java.lang.Exception
import java.net.MulticastSocket

open class JvmMulticastChannel(
    private val cipher: PayloadCipher,
    private val runner: ThreadRunner
): MulticastChannel {

    private val log by logger()
    private val receiveBuffer = ByteArray(MAX_PACKET_SIZE_BYTES)

    @Volatile
    private var socket: MulticastSocket? = null

    override fun init(receiver: PayloadReceiver) {
        require(socket == null) { "MulticastChannel already initialized" }
        socket = MulticastSocket(GROUP_PORT).apply {
            timeToLive = MULTICAST_TTL
            trafficClass = IP_TOS_THROUGHPUT
            joinGroup(GROUP_ADDRESS.inetAddress)
        }

        runner.runOnBackground {
            while(socket != null) {
                try {
                    val decryptedPayload = socket!!.awaitPayload(receiveBuffer, cipher)
                    runner.runOnMain { receiver.onReceived(decryptedPayload) }
                } catch (e: Exception) {
                    log.error(e) { "Error receiving payload" }
                }
            }
        }
    }

    override fun broadcast(payload: ByteArray) {
        log.debug { "Broadcasting payload" }
        runner.runOnBackground {
            val encryptedPayload = cipher.encrypt(payload)
            val packet = createDatagramPacket(encryptedPayload, GROUP_ADDRESS, GROUP_PORT)
            socket!!.send(packet)
        }
    }

    override fun close() {
        log.debug { "Closing MulticastChannel" }
        runner.cancelAll()
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