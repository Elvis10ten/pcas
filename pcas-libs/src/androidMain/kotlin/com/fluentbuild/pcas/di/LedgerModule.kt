package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.ledger.*

class LedgerModule(
    private val ioModule: IoModule,
    private val hostModule: HostModule,
    private val utilsModule: UtilsModule,
    private val asyncModule: AsyncModule
) {

    private val ledgerStore: LedgerDb by lazy { LedgerDb(utilsModule.timeProvider) }

    private val ledgerMessageSender: LedgerMessageSender by lazy {
        LedgerMessageSender(
            protoBuf = utilsModule.protoBuf,
            multicastChannel = ioModule.multicastChannel,
            ledgerStore = ledgerStore,
            timeProvider = utilsModule.timeProvider
        )
    }

    private val ledgerMessageReceiver: LedgerMessageReceiver by lazy {
        LedgerMessageReceiver(
            protoBuf = utilsModule.protoBuf,
            packetBroadcaster = ledgerMessageSender,
            ledgerStore = ledgerStore
        )
    }

    private val ledgerWatchdog: LedgerWatchdog by lazy {
        LedgerWatchdog(
            ledgerStore = ledgerStore,
            packetBroadcaster = ledgerMessageSender,
            timerProvider = utilsModule.timeProvider,
            runner = asyncModule.provideThreadExecutor()
        )
    }

    val ledgerProtocol: LedgerProtocol by lazy {
        LedgerProtocol(
            multicastChannel = ioModule.multicastChannel,
            hostInfoWatcher = hostModule.selfHostInfoWatcher,
            packetBroadcaster = ledgerMessageSender,
            packetReceiver = ledgerMessageReceiver,
            ledgerWatchdog = ledgerWatchdog,
            ledgerStore = ledgerStore
        )
    }
}