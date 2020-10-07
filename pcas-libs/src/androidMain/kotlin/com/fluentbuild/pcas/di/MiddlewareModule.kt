package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.conflicts.ConflictsResolver
import com.fluentbuild.pcas.Engine
import com.fluentbuild.pcas.stream.StreamDemux

internal class MiddlewareModule(
    private val ioModule: IoModule,
    private val servicesModule: ServicesModule,
    private val ledgerModule: LedgerModule
) {

    private val conflictsResolver: ConflictsResolver by lazy { ConflictsResolver() }

    private val streamDemux: StreamDemux by lazy {
        StreamDemux(
            unicast = ioModule.unicastChannel,
            serviceHandlers = servicesModule.routerServers
        )
    }

    val engine: Engine by lazy {
        Engine(
            ledgerProtocol = ledgerModule.ledgerProtocol,
            serviceHandlers = servicesModule.serviceHandlers,
            streamDemux = streamDemux,
            conflictsResolver = conflictsResolver
        )
    }
}