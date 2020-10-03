package com.fluentbuild.pcas.io

internal fun interface MessageReceiver {

    fun onReceived(message: ByteArray, messageSize: Int)
}