package com.fluentbuild.pcas.utils

import com.fluentbuild.pcas.Uuid
import java.nio.ByteBuffer
import java.util.*

object UuidCreator {

	fun create() = Uuid(UUID.randomUUID().toByteArray())

	private fun UUID.toByteArray(): ByteArray {
		val bb: ByteBuffer = ByteBuffer.wrap(ByteArray(16))
		bb.putLong(mostSignificantBits)
		bb.putLong(leastSignificantBits)
		return bb.array()
	}

	private fun Uuid.getJavaUuid(): UUID {
		val byteBuffer: ByteBuffer = ByteBuffer.wrap(bytes)
		val high: Long = byteBuffer.long
		val low: Long = byteBuffer.long
		return UUID(high, low)
	}
}