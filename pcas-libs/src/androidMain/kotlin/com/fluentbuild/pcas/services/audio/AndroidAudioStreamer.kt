package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.HostInfo
import com.fluentbuild.pcas.logs.getLog

class AndroidAudioStreamer: AudioStreamer {

    private val log = getLog()

    override fun start(destination: HostInfo) {
        log.debug(::start, destination)
    }

    override fun stop() {
        log.debug(::stop)
    }
}