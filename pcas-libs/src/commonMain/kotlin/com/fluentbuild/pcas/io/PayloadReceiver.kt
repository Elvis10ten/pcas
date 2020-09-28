package com.fluentbuild.pcas.io

fun interface PayloadReceiver {

    fun onReceived(data: ByteArray)
}