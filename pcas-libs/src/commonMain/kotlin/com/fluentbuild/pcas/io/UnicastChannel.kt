package com.fluentbuild.pcas.io

import com.fluentbuild.pcas.host.HostInfo

internal interface UnicastChannel {

    @Throws(Exception::class)
    fun init(receiver: MessageReceiver)

    @Throws(Exception::class)
    fun send(recipient: HostInfo, message: ByteArray, messageSize: Int)

    @Throws(Exception::class)
    fun getPort(): Port

    fun close()
}