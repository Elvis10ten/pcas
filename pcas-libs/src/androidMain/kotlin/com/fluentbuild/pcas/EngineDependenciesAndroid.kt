package com.fluentbuild.pcas

import com.fluentbuild.pcas.contention.ContentionsResolver
import com.fluentbuild.pcas.ledger.LedgerProtocol
import com.fluentbuild.pcas.stream.StreamDemux

internal class EngineDependenciesAndroid: EngineDependencies {

	override val ledgerProtocol: LedgerProtocol
		get() = TODO("Not yet implemented")
	override val streamDemux: StreamDemux
		get() = TODO("Not yet implemented")
	override val contentionsResolver: ContentionsResolver
		get() = TODO("Not yet implemented")

	override fun init() {
		TODO("Not yet implemented")
	}

	override fun release() {
		contentionsResolver.release()
	}
}