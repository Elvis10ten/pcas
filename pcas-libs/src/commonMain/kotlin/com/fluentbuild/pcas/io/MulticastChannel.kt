package com.fluentbuild.pcas.io

internal interface MulticastChannel {

    @Throws(Exception::class)
    fun init(receiver: MessageReceiver)

    @Throws(Exception::class)
    fun broadcast(message: MarshalledMessage, messageSize: MarshalledMessageSize)

    fun close()
}