package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.utils.logger

class AndroidAudioRouterServer: AudioRouterServer {

    private val log by logger()

    override fun onReceived(sender: HostInfo, payload: ByteArray) {
        log.debug(::onReceived, sender)
    }

    override fun release() {
        log.debug(::release)
    }

}