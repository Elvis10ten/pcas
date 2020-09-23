package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.host.HostInfoWatcher
import com.fluentbuild.pcas.async.Cancellables
import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.io.MulticastChannel
import com.fluentbuild.pcas.ledger.models.PropertyEntity
import com.fluentbuild.pcas.ledger.models.BondEntity
import com.fluentbuild.pcas.ledger.models.Ledger
import com.fluentbuild.pcas.utils.TimeProvider
import com.fluentbuild.pcas.utils.logger

class LedgerProtocol(
    private val multicastChannel: MulticastChannel,
    private val hostInfoWatcher: HostInfoWatcher,
    private val sender: PacketSender,
    private val receiver: PacketReceiver,
    private val ledgerWatchdog: LedgerWatchdog,
    private val timeProvider: TimeProvider
) {

    private val log by logger()
    private val cancellables = Cancellables()
    private lateinit var ledgerStore: LedgerStore
    private lateinit var self: HostInfo


    fun init(onChanged: (Ledger) -> Unit) {
        log.info { "Initializing LedgerProtocol" }
        cancellables += hostInfoWatcher.watch(::onSelfHostInfoUpdate)
        ledgerStore = LedgerStore(self, onChanged, timeProvider)
        multicastChannel.init(receiver::onReceived)
        ledgerWatchdog.start()
    }

    fun close() {
        log.info { "Closing LedgerProtocol" }
        ledgerWatchdog.stop()
        multicastChannel.close()
        cancellables.cancel()
        ledgerStore.clear()
    }

    fun updateBonds(bonds: Set<BondEntity>) {
        ledgerStore.upsertBonds(self, bonds)
        sender.sendUpdate(ledgerStore.get())
    }

    fun updateProps(props: Set<PropertyEntity>) {
        ledgerStore.upsertProps(self, props)
        sender.sendUpdate(ledgerStore.get())
    }

    private fun onSelfHostInfoUpdate(host: HostInfo) {
        this.self = host
        ledgerStore.upsert(newHostInfo, connections, characteristics, true)
    }
}