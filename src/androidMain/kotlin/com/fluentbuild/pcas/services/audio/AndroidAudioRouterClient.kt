package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.utils.logger

class AndroidAudioRouterClient: AudioRouterClient {

    private val log by logger()

    override fun start(remoteSink: HostInfo) {
        log.debug(::start, remoteSink)
    }

    override fun stop() {
        log.debug(::stop)
    }
}