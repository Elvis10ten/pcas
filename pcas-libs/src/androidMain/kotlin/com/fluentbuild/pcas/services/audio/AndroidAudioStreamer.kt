package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.logs.logger

class AndroidAudioStreamer: AudioStreamer {

    private val log by logger()

    override fun start(destination: HostInfo) {
        log.debug(::start, destination)
    }

    override fun stop() {
        log.debug(::stop)
    }
}