package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.services.ServiceId
import com.fluentbuild.pcas.middleware.ResolutionHandler
import com.fluentbuild.pcas.stream.StreamHandler

class ServicesModule(
    private val audioServiceModule: AudioServiceModule
) {

    val serviceHandlers: Map<ServiceId, ResolutionHandler> by lazy {
        mapOf(
            audioServiceModule.serviceId to audioServiceModule.commandHandler
        )
    }

    val routerServers: Map<ServiceId, StreamHandler> by lazy {
        mapOf(
            audioServiceModule.serviceId to audioServiceModule.streamHandler
        )
    }
}