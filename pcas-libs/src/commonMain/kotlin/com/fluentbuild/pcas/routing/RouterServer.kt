package com.fluentbuild.pcas.routing

import com.fluentbuild.pcas.host.HostInfo

interface RouterServer {

    fun onReceived(sender: HostInfo, payload: ByteArray)

    fun release()
}