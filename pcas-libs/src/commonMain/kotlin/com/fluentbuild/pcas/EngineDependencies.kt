package com.fluentbuild.pcas

import com.fluentbuild.pcas.contention.ContentionsResolver
import com.fluentbuild.pcas.ledger.LedgerProtocol
import com.fluentbuild.pcas.stream.StreamDemux

internal interface EngineDependencies {

	val ledgerProtocol: LedgerProtocol

	val streamDemux: StreamDemux

	val contentionsResolver: ContentionsResolver

	fun init()

	fun release()
}