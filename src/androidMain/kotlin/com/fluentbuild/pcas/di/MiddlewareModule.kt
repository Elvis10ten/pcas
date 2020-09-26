package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.middleware.ServiceRegistry
import com.fluentbuild.pcas.middleware.UpdateInterceptor
import com.fluentbuild.pcas.middleware.conflicts.ConflictsInterceptor
import com.fluentbuild.pcas.middleware.routing.RoutingInterceptor

class MiddlewareModule(
    private val servicesModule: ServicesModule,
    private val ledgerModule: LedgerModule
) {

    private val interceptors: List<UpdateInterceptor> by lazy {
        listOf(ConflictsInterceptor(), RoutingInterceptor())
    }

    internal val serviceRegistry: ServiceRegistry by lazy {
        ServiceRegistry(
            ledgerProtocol = ledgerModule.ledgerProtocol,
            interceptors = interceptors,
            serviceHandlers = servicesModule.serviceHandlers
        )
    }
}