package com.fluentbuild.pcas.host

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
class Uuid(
	@ProtoNumber(1)
	val bytes: ByteArray
) {

	companion object {

		internal const val BYTES_SIZE = 16

		fun create(bytes: ByteArray, offset: Int = 0) = Uuid(bytes.copyOfRange(offset, BYTES_SIZE))
	}
}