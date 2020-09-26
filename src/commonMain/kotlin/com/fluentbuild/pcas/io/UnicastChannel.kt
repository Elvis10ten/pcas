package com.fluentbuild.pcas.io

import com.fluentbuild.pcas.host.HostInfo

interface UnicastChannel {

    fun init(receiver: PayloadReceiver)

    fun send(recipient: HostInfo, payload: ByteArray)

    fun close()
}