package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.HostInfo

internal interface AudioStreamer {

    fun start(destination: HostInfo)

    fun stop()
}