package com.fluentbuild.pcas.io

internal fun interface PayloadReceiver {

    fun onReceived(payload: ByteArray, payloadSize: Int)
}