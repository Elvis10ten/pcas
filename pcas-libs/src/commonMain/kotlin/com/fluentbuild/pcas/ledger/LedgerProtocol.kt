package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.host.HostInfoObservable
import com.fluentbuild.pcas.async.Cancellables
import com.fluentbuild.pcas.io.MulticastChannel

internal class LedgerProtocol(
    private val multicast: MulticastChannel,
    private val hostObservable: HostInfoObservable,
    private val messageSender: LedgerMessageSender,
    private val messageReceiver: LedgerMessageReceiver,
    private val ledgerWatchdog: LedgerWatchdog,
    private val ledgerDb: LedgerDb,
    private val serviceBlocksProducers: List<BlocksProducer>
) {

    fun run(onLedgerUpdated: (Ledger) -> Unit): Cancellable {
        val cancellables = Cancellables()
        ledgerDb.create(hostObservable.currentValue, onLedgerUpdated)
        multicast.init(messageReceiver::onReceived)
        messageSender.sendGenesis()

        cancellables += hostObservable.subscribe {
            ledgerDb.updateSelf(it)
            messageSender.sendUpdate()
        }

        cancellables += ledgerWatchdog.run()

        val consumer = { blocks: Set<Block> ->
            ledgerDb.upsert(blocks)
            messageSender.sendUpdate()
        }
        cancellables += serviceBlocksProducers.map { it.subscribe(consumer) }

        return Cancellable {
            cancellables.cancel()
            multicast.close()
            ledgerDb.destroy()
        }
    }
}