package com.fluentbuild.pcas.stream

import com.fluentbuild.pcas.Uuid
import com.fluentbuild.pcas.io.MarshalledMessageSize

internal interface StreamHandler {

    fun handle(sender: Uuid, payload: ByteArray, offset: Int, size: MarshalledMessageSize)

    fun release()
}