package com.fluentbuild.pcas.stream

import com.fluentbuild.pcas.host.Uuid
import com.fluentbuild.pcas.io.MarshalledMessage
import com.fluentbuild.pcas.io.MarshalledMessageSize

/**
 * For performance reasons, we are reading values directly from the [MarshalledMessage].
 */

private const val INDEX_SERVICE_ID = 0
private const val SERVICE_ID_SIZE = 1
private const val INDEX_SENDER_UUID = INDEX_SERVICE_ID + SERVICE_ID_SIZE
internal const val PAYLOAD_OFFSET = INDEX_SENDER_UUID + Uuid.BYTES_SIZE

internal val MarshalledMessage.serviceId get() = this[INDEX_SERVICE_ID].toInt()

internal val MarshalledMessage.senderUuid get() = Uuid.create(this, INDEX_SENDER_UUID)

internal val MarshalledMessageSize.payloadSize get() = this - PAYLOAD_OFFSET
