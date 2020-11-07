package com.fluentbuild.pcas.io.transport

internal interface MulticastChannel {

    @Throws(Exception::class)
    fun init(receiver: MessageReceiver)

    @Throws(Exception::class)
    fun send(parcel: ByteArray, size: Int)

    fun close()
}