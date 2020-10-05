package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.host.HostUuid
import com.fluentbuild.pcas.logs.logger

class AndroidAudioStreamHandler: AudioStreamHandler {

    private val log by logger()

    override fun handle(senderUuid: HostUuid, data: ByteArray, offset: Int, size: Int) {
        TODO("Not yet implemented")
    }

    override fun release() {
        log.debug(::release)
    }

}