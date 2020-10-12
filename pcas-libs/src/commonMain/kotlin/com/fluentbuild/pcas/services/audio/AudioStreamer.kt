package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.host.HostInfo

internal interface AudioStreamer {

    fun start(destination: HostInfo)

    fun stop()
}