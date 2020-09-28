package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.ledger.models.ServiceId
import com.fluentbuild.pcas.middleware.CommandHandler
import com.fluentbuild.pcas.middleware.routing.RouterServer

class ServicesModule(
    private val audioServiceModule: AudioServiceModule
) {

    val serviceHandlers: Map<ServiceId, CommandHandler> by lazy {
        mapOf(
            audioServiceModule.serviceId to audioServiceModule.commandHandler
        )
    }

    val routerServers: Map<ServiceId, RouterServer> by lazy {
        mapOf(
            audioServiceModule.serviceId to audioServiceModule.routerServer
        )
    }
}