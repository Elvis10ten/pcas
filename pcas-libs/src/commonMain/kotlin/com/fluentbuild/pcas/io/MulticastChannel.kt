package com.fluentbuild.pcas.io

internal interface MulticastChannel {

    @Throws(Exception::class)
    fun init(receiver: PayloadReceiver)

    @Throws(Exception::class)
    fun broadcast(payload: ByteArray, payloadSize: Int)

    fun close()
}