package com.fluentbuild.pcas.io

import java.util.ArrayDeque
import com.fluentbuild.pcas.io.transport.TransportConfig.MAX_PARCEL_SIZE_BYTES
import com.fluentbuild.pcas.io.transport.TransportConfig.PARCEL_POOL_CAPACITY

internal object BufferObjectPool {

	private val buffers = ArrayDeque<ByteArray>(PARCEL_POOL_CAPACITY)

	@Synchronized
	fun allocate(): ByteArray = buffers.poll() ?: ByteArray(MAX_PARCEL_SIZE_BYTES)

	@Synchronized
	fun recycle(buffer: ByteArray) {
		if(buffers.size <= PARCEL_POOL_CAPACITY) {
			buffers.offer(buffer)
		}
	}
}