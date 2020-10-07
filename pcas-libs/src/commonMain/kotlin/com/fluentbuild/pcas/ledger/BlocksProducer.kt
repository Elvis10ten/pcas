package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.async.Cancellable

internal fun interface BlocksProducer {

	fun subscribe(consumer: (Set<Block>) -> Unit): Cancellable
}