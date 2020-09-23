package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.host.HostInfo

interface AudioRouter {

    fun start(remoteSink: HostInfo)

    fun stop()
}