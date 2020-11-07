package com.fluentbuild.pcas.io

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.io.transport.IpTos
import com.fluentbuild.pcas.io.transport.MessageReceiver
import com.fluentbuild.pcas.io.transport.UnicastChannel
import java.io.IOException
import java.net.DatagramSocket

internal open class UnicastChannelJvm(
    private val socketWrapper: SocketWrapper<DatagramSocket>
): UnicastChannel {

    @Throws(IOException::class)
    override fun init(receiver: MessageReceiver) {
        socketWrapper.init(DatagramSocket()) {
            trafficClass = IpTos.LOW_DELAY.value
        }
        socketWrapper.startReceiving(receiver)
    }

    @Throws(IOException::class)
    override fun send(recipient: HostInfo, parcel: ByteArray, messageSize: Int) {
        socketWrapper.send(parcel, messageSize, recipient.address, recipient.port)
    }

    @Throws(IOException::class)
    override fun getPort() = socketWrapper.getPort()

    override fun close() {
        socketWrapper.close()
    }
}