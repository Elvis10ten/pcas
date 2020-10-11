package com.fluentbuild.pcas

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.Cancellables
import com.fluentbuild.pcas.ledger.LedgerProtocol
import com.fluentbuild.pcas.stream.StreamDemux
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.contention.ContentionsResolver

class Engine internal constructor(
	private val ledgerProtocol: LedgerProtocol,
	private val streamDemux: StreamDemux,
	private val contentionsResolver: ContentionsResolver
) {

    private val log = getLog()

    fun run(): Cancellable {
        log.info { "Running engine!" }
        val cancellables = Cancellables()

        cancellables += streamDemux.run()
        cancellables += ledgerProtocol.run { ledger ->
			log.info { "Ledger updated" }
			contentionsResolver.resolve(ledger)
		}

        return Cancellable {
            log.info { "Stopping engine!" }
			contentionsResolver.release()
            cancellables.cancel()
        }
    }
}