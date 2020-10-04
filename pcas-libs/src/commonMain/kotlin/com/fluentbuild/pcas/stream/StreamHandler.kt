package com.fluentbuild.pcas.stream

import com.fluentbuild.pcas.host.HostUuid

interface StreamHandler {

    fun handle(senderUuid: HostUuid, data: ByteArray, offset: Int, size: Int)

    fun release()
}