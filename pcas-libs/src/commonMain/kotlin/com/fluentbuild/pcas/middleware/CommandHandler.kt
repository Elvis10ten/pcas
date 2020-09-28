package com.fluentbuild.pcas.middleware

interface CommandHandler {

    fun handle(command: Command)

    fun release()
}