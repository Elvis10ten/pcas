package com.fluentbuild.pcas.io

import com.fluentbuild.pcas.host.HostInfo

internal interface UnicastChannel {

    @Throws(Exception::class)
    fun init(receiver: PayloadReceiver)

    @Throws(Exception::class)
    fun send(recipient: HostInfo, payload: ByteArray, payloadSize: Int)

    @Throws(Exception::class)
    fun getPort(): Port

    fun close()
}