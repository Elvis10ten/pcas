package com.fluentbuild.pcas.io

import com.fluentbuild.pcas.host.HostInfo

interface UnicastChannel {

    fun init(receiver: ParcelReceiver)

    fun send(recipient: HostInfo, parcel: Parcel)

    fun close()
}