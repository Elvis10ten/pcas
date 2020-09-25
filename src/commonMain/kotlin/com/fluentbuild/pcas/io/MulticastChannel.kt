package com.fluentbuild.pcas.io

interface MulticastChannel {

    fun init(receiver: ParcelReceiver)

    fun broadcast(payload: ByteArray)

    fun close()
}