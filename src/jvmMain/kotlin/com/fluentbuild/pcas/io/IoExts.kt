package com.fluentbuild.pcas.io

import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.*
import kotlin.jvm.Throws

internal const val IP_TOS_THROUGHPUT = 0x08
internal const val MAX_PACKET_SIZE_BYTES = 16 * 1024 // 16KB

internal val Address.Ipv4.inetAddress: InetAddress
    get() = InetAddress.getByName(ipDottedFormat)

@Throws(IOException::class)
internal fun DatagramSocket.awaitPayload(receiveBuffer: ByteArray, cipher: PayloadCipher): ByteArray {
    val packet = DatagramPacket(receiveBuffer, receiveBuffer.size)
    receive(packet)
    val payload = Arrays.copyOfRange(packet.data, packet.offset, packet.length)
    return cipher.decrypt(payload)
}

internal fun createDatagramPacket(buffer: ByteArray, address: Address.Ipv4, port: Port) =
    DatagramPacket(buffer, buffer.size, address.inetAddress, port)