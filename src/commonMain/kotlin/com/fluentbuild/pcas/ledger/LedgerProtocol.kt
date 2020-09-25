package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.host.HostInfoWatcher
import com.fluentbuild.pcas.async.Cancellables
import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.io.MulticastChannel
import com.fluentbuild.pcas.ledger.models.PropertyEntity
import com.fluentbuild.pcas.ledger.models.BondEntity
import com.fluentbuild.pcas.ledger.models.Ledger
import com.fluentbuild.pcas.utils.logger

class LedgerProtocol(
    private val multicastChannel: MulticastChannel,
    private val hostInfoWatcher: HostInfoWatcher,
    private val packetBroadcaster: PacketBroadcaster,
    private val packetReceiver: PacketReceiver,
    private val ledgerWatchdog: LedgerWatchdog,
    private val ledgerStore: LedgerStore
) {

    private val log by logger()
    private val cancellables = Cancellables()


    fun init(onLedgerChanged: (Ledger) -> Unit) {
        log.info { "Initializing LedgerProtocol" }
        ledgerStore.setup(hostInfoWatcher.currentValue, onLedgerChanged)
        cancellables += hostInfoWatcher.watch(::onHostInfoChanged)

        multicastChannel.init(packetReceiver::onReceived)
        packetBroadcaster.broadcastIntro()

        ledgerWatchdog.start()
    }

    fun close() {
        log.info { "Closing LedgerProtocol" }
        ledgerWatchdog.stop()
        cancellables.cancel()
        multicastChannel.close()
        ledgerStore.clear()
    }

    fun updateBonds(bonds: Set<BondEntity>) {
        ledgerStore.upsertBonds(hostInfoWatcher.currentValue, bonds)
        packetBroadcaster.broadcastUpdate()
    }

    fun updateProps(props: Set<PropertyEntity>) {
        ledgerStore.upsertProps(hostInfoWatcher.currentValue, props)
        packetBroadcaster.broadcastUpdate()
    }

    private fun onHostInfoChanged(self: HostInfo) {
        ledgerStore.updateSelf(self)
    }
}