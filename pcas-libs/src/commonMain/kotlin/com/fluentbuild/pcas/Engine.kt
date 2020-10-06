package com.fluentbuild.pcas

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.Cancellables
import com.fluentbuild.pcas.ledger.LedgerProtocol
import com.fluentbuild.pcas.stream.StreamDemux
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.middleware.ConflictsResolver

class Engine internal constructor(
	private val ledgerProtocol: LedgerProtocol,
	private val streamDemux: StreamDemux,
	private val conflictsResolver: ConflictsResolver
) {

    private val log = getLog()

    fun run(): Cancellable {
        log.info { "Running engine!" }
        val cancellables = Cancellables()

        cancellables += streamDemux.run()
        cancellables += ledgerProtocol.run { ledger ->
			log.info { "Ledger updated" }
			conflictsResolver.resolve(ledger)
		}

        return Cancellable {
            log.info { "Stopping engine!" }
			conflictsResolver.release()
            cancellables.cancel()
        }
    }
}