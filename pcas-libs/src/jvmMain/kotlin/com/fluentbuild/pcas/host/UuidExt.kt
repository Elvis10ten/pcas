package com.fluentbuild.pcas.host

import java.nio.ByteBuffer
import java.util.*

fun UUID.getHostUuid(): ByteArray {
	val bb: ByteBuffer = ByteBuffer.wrap(ByteArray(16))
	bb.putLong(mostSignificantBits)
	bb.putLong(leastSignificantBits)
	return bb.array()
}

fun Uuid.getJavaUuid(): UUID {
	val byteBuffer: ByteBuffer = ByteBuffer.wrap(bytes)
	val high: Long = byteBuffer.long
	val low: Long = byteBuffer.long
	return UUID(high, low)
}