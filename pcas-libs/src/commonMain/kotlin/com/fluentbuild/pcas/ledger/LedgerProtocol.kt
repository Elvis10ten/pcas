package com.fluentbuild.pcas.ledger

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

    fun init(onLedgerUpdated: (Ledger) -> Unit) {
        ledgerDb.create(hostObservable.currentValue, onLedgerUpdated)

        cancellables += hostObservable.subscribe {
            ledgerDb.updateSelf(it)
            messageSender.sendUpdate()
        }

        multicast.init(messageReceiver::onReceived)
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