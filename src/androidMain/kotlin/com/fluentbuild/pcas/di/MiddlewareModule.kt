package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.middleware.ServiceRegistry
import com.fluentbuild.pcas.middleware.UpdateInterceptor
import com.fluentbuild.pcas.middleware.conflicts.ConflictsInterceptor
import com.fluentbuild.pcas.middleware.routing.RouterClientInterceptor
import com.fluentbuild.pcas.middleware.routing.RouterServerDeMultiplexer

class MiddlewareModule(
    private val ioModule: IoModule,
    private val servicesModule: ServicesModule,
    private val ledgerModule: LedgerModule,
    private val utilsModule: UtilsModule
) {

    private val interceptors: List<UpdateInterceptor> by lazy {
        listOf(ConflictsInterceptor(), RouterClientInterceptor())
    }

    private val routerServerDeMultiplexer: RouterServerDeMultiplexer by lazy {
        RouterServerDeMultiplexer(
            protoBuf = utilsModule.protoBuf,
            unicastChannel = ioModule.unicastChannel,
            serviceRouters = servicesModule.routerServers
        )
    }
    val serviceRegistry: ServiceRegistry by lazy {
        ServiceRegistry(
            ledgerProtocol = ledgerModule.ledgerProtocol,
            interceptors = interceptors,
            serviceHandlers = servicesModule.serviceHandlers,
            routerServerDeMultiplexer = routerServerDeMultiplexer
        )
    }
}