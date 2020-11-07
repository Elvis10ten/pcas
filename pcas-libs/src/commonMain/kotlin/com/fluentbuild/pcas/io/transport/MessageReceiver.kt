package com.fluentbuild.pcas.io.transport

internal fun interface MessageReceiver {

    fun onReceived(parcel: ByteArray, size: Int)
}