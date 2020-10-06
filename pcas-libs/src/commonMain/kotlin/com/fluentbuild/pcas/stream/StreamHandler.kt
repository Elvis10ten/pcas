package com.fluentbuild.pcas.stream

import com.fluentbuild.pcas.host.Uuid
import com.fluentbuild.pcas.io.MarshalledMessageSize

interface StreamHandler {

    fun handle(sender: Uuid, payload: ByteArray, offset: Int, size: MarshalledMessageSize)

    fun release()
}