package com.fluentbuild.pcas.utils

import com.fluentbuild.pcas.io.transport.TransportConfig.OFFSET_ZERO
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.serializer

internal inline fun <reified T> BinaryFormat.decode(message: ByteArray, size: Int): T =
	decodeFromByteArray(serializersModule.serializer(), message.copyOfRange(OFFSET_ZERO, size))