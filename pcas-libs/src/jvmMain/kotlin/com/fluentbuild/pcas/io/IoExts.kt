package com.fluentbuild.pcas.io

import com.fluentbuild.pcas.logs.Log
import java.io.Closeable
import java.io.IOException
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketException

internal inline val Address.Ipv4.inetAddress: InetAddress get() = InetAddress.getByName(quadDottedDecimalValue)

internal fun Closeable.closeQuietly(log: Log) {
    try {
        close()
    } catch (e: Exception) {
        log.error(e) { "Error closing closeable" }
    }
}

internal fun DatagramSocket?.canReceive() = isOpenAndBounded() && !Thread.currentThread().isInterrupted

internal fun DatagramSocket?.isOpenAndBounded() = this?.isClosed == false && isBound

internal fun DatagramSocket?.requireOpenAndBounded(): Unit =
    require(isOpenAndBounded()) { "Socket is closed or unbounded" }

@Throws(IOException::class, NullPointerException::class)
internal fun DatagramSocket.requirePort(): Port {
    val port = localPort
    return if(port <= 0) {
        throw SocketException("Socket with port($port) is closed or not bounded")
    } else {
        port
    }
}