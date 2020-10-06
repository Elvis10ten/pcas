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
    private val ledgerDb: LedgerDb
) {

    private val cancellables = Cancellables()

    fun run(onLedgerUpdated: (Ledger) -> Unit): Cancellable {
        blocksProducer: List<BlocksProducer>,
        ledgerDb.create(hostObservable.currentValue, onLedgerUpdated)
        multicast.init(messageReceiver::onReceived)

        cancellables += hostObservable.subscribe {
            ledgerDb.updateSelf(it)
            messageSender.sendUpdate()
        }

        messageSender.sendGenesis()
        cancellables += ledgerWatchdog.run()
    }

    fun close() {
        messageSender.sendExodus()
        cancellables.cancel()
        multicast.close()
        ledgerDb.destroy()
    }

    fun updateBlocks(blocks: Set<Block>) {
        ledgerDb.upsert(blocks)
        messageSender.sendUpdate()
    }
}