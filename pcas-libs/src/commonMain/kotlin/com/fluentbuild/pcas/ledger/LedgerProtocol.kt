package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.host.HostInfoObservable
import com.fluentbuild.pcas.async.Cancellables
import com.fluentbuild.pcas.io.transport.MulticastChannel
import com.fluentbuild.pcas.ledger.messages.MessageReceiver
import com.fluentbuild.pcas.ledger.messages.MessageSender
import com.fluentbuild.pcas.values.Observable

internal class LedgerProtocol(
	private val multicastChannel: MulticastChannel,
	private val hostObservable: HostInfoObservable,
	private val messageSender: MessageSender,
	private val messageReceiver: MessageReceiver,
	private val ledgerWatchdog: LedgerWatchdog,
	private val ledgerDb: LedgerDb,
	private val serviceBlocksObservables: List<Observable<Set<Block>>>
) {

    fun run(onLedgerUpdated: (Ledger) -> Unit): Cancellable {
        ledgerDb.create(hostObservable.currentValue, onLedgerUpdated)
        multicastChannel.init(messageReceiver::onReceived)
        messageSender.sendGenesis()
		val cancellables = startCancellables()

        return Cancellable {
            cancellables.cancel()
			messageSender.cancel()
            multicastChannel.close()
            ledgerDb.destroy()
        }
    }

	private fun startCancellables(): Cancellable {
		val cancellables = Cancellables()
		cancellables += hostObservable.subscribe {
			ledgerDb.updateSelf(it)
			messageSender.sendUpdate()
		}

		val consumer = { blocks: Set<Block> ->
			ledgerDb.upsert(blocks)
			messageSender.sendUpdate()
		}
		cancellables += serviceBlocksObservables.map { it.subscribe(consumer) }

		cancellables += ledgerWatchdog.run()
		return cancellables
	}
}