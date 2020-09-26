package com.fluentbuild.pcas.io

interface MulticastChannel {

    fun init(receiver: PayloadReceiver)

    fun broadcast(payload: ByteArray)

    fun close()
}