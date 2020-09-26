package com.fluentbuild.pcas.middleware

import com.fluentbuild.pcas.ledger.models.Ledger

fun interface UpdateInterceptor {

    @Throws(RuntimeException::class)
    fun intercept(ledger: Ledger): Set<Command>
}