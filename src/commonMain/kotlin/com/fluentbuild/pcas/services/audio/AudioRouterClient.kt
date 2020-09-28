package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.host.HostInfo

interface AudioRouterClient {

    fun start(remoteSink: HostInfo)

    fun stop()
}