package com.fluentbuild.pcas.io

import com.fluentbuild.pcas.host.HostInfo
import java.io.IOException
import java.net.DatagramSocket

internal open class SecuredUnicastChannel(
    private val socketWrapper: SocketWrapper<DatagramSocket>
): UnicastChannel {

    @Throws(IOException::class)
    override fun init(receiver: MessageReceiver) {
        socketWrapper.init(DatagramSocket())
        socketWrapper.startReceiving(receiver)
    }

    @Throws(IOException::class)
    override fun send(recipient: HostInfo, message: ByteArray, messageSize: Int) {
        socketWrapper.send(message, messageSize, recipient.address, recipient.port)
    }

    @Throws(IOException::class)
    override fun getPort() = socketWrapper.getPort()

    override fun close() {
        socketWrapper.close()
    }
}