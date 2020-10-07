package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.host.Uuid
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.stream.StreamHandler

class AndroidAudioStreamHandler: StreamHandler {

    private val log = getLog()

    override fun handle(sender: Uuid, payload: ByteArray, offset: Int, size: Int) {
        TODO("Not yet implemented")
    }

    override fun release() {
        log.debug(::release)
    }

}