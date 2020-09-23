package com.fluentbuild.pcas.middleware

import com.fluentbuild.pcas.middleware.resolvers.Resolution

interface ResolutionHandler {

    fun handle(resolution: Resolution)

    fun release()
}