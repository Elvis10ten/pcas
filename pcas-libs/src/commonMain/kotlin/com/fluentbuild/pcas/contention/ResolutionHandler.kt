package com.fluentbuild.pcas.contention

internal interface ResolutionHandler {

    fun handle(resolution: Contention.Resolution)

    fun release()
}