package com.fluentbuild.pcas.utils

import com.fluentbuild.pcas.io.MarshalledMessage
import com.fluentbuild.pcas.io.MarshalledMessageSize
import com.fluentbuild.pcas.io.OFFSET_ZERO
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.serializer

internal inline fun <reified T> BinaryFormat.decode(message: MarshalledMessage, size: MarshalledMessageSize): T =
	decodeFromByteArray(serializersModule.serializer(), message.copyOfRange(OFFSET_ZERO, size))