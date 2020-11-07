package com.fluentbuild.pcas.io.transport

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.io.Port

internal interface UnicastChannel {

    @Throws(Exception::class)
    fun init(receiver: MessageReceiver)

    @Throws(Exception::class)
    fun send(recipient: HostInfo, parcel: ByteArray, size: Int)

    @Throws(Exception::class)
    fun getPort(): Port

    fun close()
}