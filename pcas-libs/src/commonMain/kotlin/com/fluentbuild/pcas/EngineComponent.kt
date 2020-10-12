package com.fluentbuild.pcas

import com.fluentbuild.pcas.contention.ContentionsResolver
import com.fluentbuild.pcas.ledger.LedgerProtocol
import com.fluentbuild.pcas.stream.StreamDemuxer

internal interface EngineComponent {

	val ledgerProtocol: LedgerProtocol

	val streamDemuxer: StreamDemuxer

	val contentionsResolver: ContentionsResolver

	fun release()
}