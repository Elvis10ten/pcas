package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.services.ServiceId
import com.fluentbuild.pcas.conflicts.ResolutionHandler
import com.fluentbuild.pcas.services.AUDIO_SERVICE_ID
import com.fluentbuild.pcas.stream.StreamHandler

internal class ServicesModule(
    private val audioServiceModule: AudioServiceModule
) {

    val serviceHandlers: Map<ServiceId, ResolutionHandler> by lazy {
        mapOf(
            AUDIO_SERVICE_ID to audioServiceModule.resolutionHandler
        )
    }

    val routerServers: Map<ServiceId, StreamHandler> by lazy {
        mapOf(
            AUDIO_SERVICE_ID to audioServiceModule.streamHandler
        )
    }
}