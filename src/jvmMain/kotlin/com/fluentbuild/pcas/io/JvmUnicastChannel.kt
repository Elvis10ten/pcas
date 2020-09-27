package com.fluentbuild.pcas.io

import com.fluentbuild.pcas.async.ThreadExecutor
import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.utils.logger
import java.lang.Exception
import java.net.DatagramSocket
import java.net.InetSocketAddress

open class JvmUnicastChannel(
    private val cipher: Cipher,
    private val executor: ThreadExecutor
): UnicastChannel {

    private val log by logger()
    private val receiveBuffer = ByteArray(MAX_PACKET_SIZE)

    @Volatile
    private var socket: DatagramSocket? = null

    override fun init(receiver: PayloadReceiver) {
        require(socket == null) { "UnicastChannel already initialized" }
        socket = DatagramSocket().apply {
            trafficClass = IP_TOS_THROUGHPUT
            bind(InetSocketAddress(0))
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

    override fun send(recipient: HostInfo, payload: ByteArray) {
        log.debug { "Sending payload to: $recipient" }
        executor.onBackground {
            val encryptedPayload = cipher.encrypt(payload)
            val packet = createDatagramPacket(encryptedPayload, recipient.ip, recipient.port)
            socket!!.send(packet)
        }
    }

    override fun close() {
        log.debug { "Closing UnicastChannel" }
        executor.cancel()
        socket?.close()
        socket = null
    }

}