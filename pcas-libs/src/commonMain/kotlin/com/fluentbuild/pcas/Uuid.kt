package com.fluentbuild.pcas

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
data class Uuid(
	@ProtoNumber(1)
	val bytes: ByteArray
) {

	override fun equals(other: Any?) = bytes.contentEquals((other as? Uuid)?.bytes)

	override fun hashCode() = bytes.contentHashCode()

	companion object {

		internal const val BYTES_SIZE = 16

		fun create(bytes: ByteArray, offset: Int = 0) = Uuid(bytes.copyOfRange(offset, BYTES_SIZE))
	}
}