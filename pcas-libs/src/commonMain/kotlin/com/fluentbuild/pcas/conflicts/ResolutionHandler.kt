package com.fluentbuild.pcas.conflicts

internal interface ResolutionHandler {

    fun handle(resolution: Conflict.Resolution)

    fun release()
}