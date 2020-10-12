package com.fluentbuild.pcas.io

import com.fluentbuild.pcas.host.HostInfo

internal interface SecureUnicastChannel {

    @Throws(Exception::class)
    fun init(receiver: MessageReceiver)

    @Throws(Exception::class)
    fun send(recipient: HostInfo, message: MarshalledMessage, messageSize: MarshalledMessageSize)

    @Throws(Exception::class)
    fun getPort(): Port

    fun close()
}