package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.ledger.*

class LedgerModule(
    private val ioModule: IoModule,
    private val hostModule: HostModule,
    private val utilsModule: UtilsModule,
    private val asyncModule: AsyncModule
) {

    private val ledgerStore: LedgerStore by lazy { LedgerStore(utilsModule.timeProvider) }

    private val packetBroadcaster: PacketBroadcaster by lazy {
        PacketBroadcaster(
            protoBuf = utilsModule.protoBuf,
            multicastChannel = ioModule.multicastChannel,
            ledgerStore = ledgerStore,
            timeProvider = utilsModule.timeProvider
        )
    }

    private val packetReceiver: PacketReceiver by lazy {
        PacketReceiver(
            protoBuf = utilsModule.protoBuf,
            packetBroadcaster = packetBroadcaster,
            ledgerStore = ledgerStore
        )
    }

    private val ledgerWatchdog: LedgerWatchdog by lazy {
        LedgerWatchdog(
            ledgerStore = ledgerStore,
            packetBroadcaster = packetBroadcaster,
            timerProvider = utilsModule.timeProvider,
            executor = asyncModule.provideThreadExecutor()
        )
    }

    val ledgerProtocol: LedgerProtocol by lazy {
        LedgerProtocol(
            multicastChannel = ioModule.multicastChannel,
            hostInfoWatcher = hostModule.selfHostInfoWatcher,
            packetBroadcaster = packetBroadcaster,
            packetReceiver = packetReceiver,
            ledgerWatchdog = ledgerWatchdog,
            ledgerStore = ledgerStore
        )
    }
}