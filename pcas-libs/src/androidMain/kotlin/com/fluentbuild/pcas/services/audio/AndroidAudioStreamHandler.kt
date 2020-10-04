package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.utils.logger

class AndroidAudioStreamHandler: AudioStreamHandler {

    private val log by logger()

    override fun handle(sender: HostInfo, payload: ByteArray) {
        log.debug(::handle, sender)
    }

    override fun release() {
        log.debug(::release)
    }

}