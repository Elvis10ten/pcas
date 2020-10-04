package com.fluentbuild.pcas.utils

import com.fluentbuild.pcas.io.OFFSET_ZERO
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.serializer

internal inline fun <reified T> BinaryFormat.decodeFromByteArray(marshalledMessage: ByteArray, actualSize: Int): T {
	return if(marshalledMessage.size == actualSize) {
		decodeFromByteArray(serializersModule.serializer(), marshalledMessage)
	} else {
		decodeFromByteArray(serializersModule.serializer(), marshalledMessage.copyOfRange(OFFSET_ZERO, actualSize))
	}
}