package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.stream.StreamHandler
import com.fluentbuild.pcas.stream.StreamMessage

class AudioStreamHandlerAndroid: StreamHandler {

    private val log = getLog()

    override fun handle(message: StreamMessage) {
        log.debug(::handle)
    }

    override fun release() {
        log.debug(::release)
    }

}