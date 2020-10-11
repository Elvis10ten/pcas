package com.fluentbuild.pcas.io

internal interface SecureMulticastChannel {

    @Throws(Exception::class)
    fun init(receiver: MessageReceiver)

    @Throws(Exception::class)
    fun send(message: MarshalledMessage, messageSize: MarshalledMessageSize)

    fun close()
}