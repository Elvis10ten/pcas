package com.fluentbuild.pcas.host

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
data class Uuid(
	@ProtoNumber(1)
	val bytes: ByteArray
) {

	override fun equals(other: Any?): Boolean {
		if(other !is Uuid) return false
		return bytes.contentEquals(other.bytes)
	}

	override fun hashCode() = bytes.contentHashCode()
}