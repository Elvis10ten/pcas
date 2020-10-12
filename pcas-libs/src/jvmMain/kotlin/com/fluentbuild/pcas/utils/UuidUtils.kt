package com.fluentbuild.pcas.utils

import com.fluentbuild.pcas.host.Uuid
import java.nio.ByteBuffer
import java.util.*

internal fun createRandomUuid() = Uuid(UUID.randomUUID().toByteArray())

internal fun UUID.toUuid() = Uuid(toByteArray())

internal fun UUID.toByteArray(): ByteArray {
	val bb: ByteBuffer = ByteBuffer.wrap(ByteArray(16))
	bb.putLong(mostSignificantBits)
	bb.putLong(leastSignificantBits)
	return bb.array()
}

internal fun Uuid.getJavaUuid(): UUID {
	val byteBuffer: ByteBuffer = ByteBuffer.wrap(bytes)
	val high: Long = byteBuffer.long
	val low: Long = byteBuffer.long
	return UUID(high, low)
}