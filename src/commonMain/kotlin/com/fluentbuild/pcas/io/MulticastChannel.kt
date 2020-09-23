package com.fluentbuild.pcas.io

interface MulticastChannel {

    fun init(receiver: ParcelReceiver)

    fun broadcast(parcel: Parcel)

    fun close()
}