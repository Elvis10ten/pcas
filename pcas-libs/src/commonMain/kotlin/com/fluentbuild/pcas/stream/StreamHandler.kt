package com.fluentbuild.pcas.stream

internal interface StreamHandler {

    fun handle(message: StreamMessage)

    fun release()
}