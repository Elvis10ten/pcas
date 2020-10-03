package com.fluentbuild.pcas.io

internal fun interface PayloadReceiver {

    fun onReceived(data: ByteArray)
}