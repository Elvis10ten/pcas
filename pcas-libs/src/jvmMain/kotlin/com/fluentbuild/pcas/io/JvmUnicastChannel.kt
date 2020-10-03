package com.fluentbuild.pcas.io

import com.fluentbuild.pcas.async.ThreadRunner
import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.utils.logger
import kotlinx.io.core.IoBuffer
import java.lang.Exception
import java.net.DatagramSocket

open class JvmUnicastChannel internal constructor(
    private val cipher: PayloadCipher,
    private val runner: ThreadRunner
): UnicastChannel {

    private val log by logger()
    private val receiveBuffer = ByteArray(MAX_PACKET_SIZE_BYTES)

    @Volatile
    private var socket: DatagramSocket? = null

    override fun init(receiver: PayloadReceiver) {
        require(socket == null) { "UnicastChannel already initialized" }
        socket = DatagramSocket().apply {
            trafficClass = IP_TOS_THROUGHPUT
        }

        runner.runOnIo {
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

    override fun send(recipient: HostInfo, payload: IoBuffer) {
        TODO("Not yet implemented")
    }
    override fun send(recipient: HostInfo, payload: ByteArray) {
        log.debug { "Sending payload to: $recipient" }
        runner.runOnIo {
            val encryptedPayload = cipher.encrypt(payload)
            val packet = createDatagramPacket(encryptedPayload, recipient.address, recipient.port)
            socket!!.send(packet)
        }
    }

    override fun getPort(): Port {
        return socket!!.localPort
    }
    override fun close() {
        log.debug { "Closing UnicastChannel" }
        runner.cancelAll()
        socket?.close()
        socket = null
    }

}