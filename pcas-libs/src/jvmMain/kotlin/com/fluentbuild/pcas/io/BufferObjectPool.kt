package com.fluentbuild.pcas.io

import java.util.ArrayDeque

internal object BufferObjectPool {

	private val buffers = ArrayDeque<ByteArray>(BUFFER_POOL_CAPACITY)

	@Synchronized
	fun allocate(): ByteArray = buffers.poll() ?: ByteArray(MAX_PACKET_SIZE_BYTES)

	@Synchronized
	fun recycle(buffer: ByteArray) {
		if(buffers.size <= BUFFER_POOL_CAPACITY) {
			buffers.offer(buffer)
		}
	}
}