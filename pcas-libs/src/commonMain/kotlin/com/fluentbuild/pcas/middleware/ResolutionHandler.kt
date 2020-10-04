package com.fluentbuild.pcas.middleware

internal interface ResolutionHandler {

    fun handle(resolution: Conflict.Resolution)

    fun release()
}