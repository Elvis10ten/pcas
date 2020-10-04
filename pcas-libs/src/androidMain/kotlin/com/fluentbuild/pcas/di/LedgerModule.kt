package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.ledger.*

class LedgerModule(
    private val ioModule: IoModule,
    private val hostModule: HostModule,
    private val utilsModule: UtilsModule,
    private val asyncModule: AsyncModule
) {

    private val ledgerDb: LedgerDb by lazy { LedgerDb() }

    private val ledgerMessageSender: LedgerMessageSender by lazy {
        LedgerMessageSender(
            protoBuf = utilsModule.protoBuf,
            multicast = ioModule.multicastChannel,
            ledgerDb = ledgerDb
        )
    }

    private val ledgerWatchdog: LedgerWatchdog by lazy {
        LedgerWatchdog(
            runner = asyncModule.provideThreadExecutor(),
            ledgerDb = ledgerDb,
            messageSender = ledgerMessageSender,
            timeProvider = utilsModule.timeProvider,
        )
    }

    private val ledgerMessageReceiver: LedgerMessageReceiver by lazy {
        LedgerMessageReceiver(
            protoBuf = utilsModule.protoBuf,
            messageSender = ledgerMessageSender,
            ledgerDb = ledgerDb,
            watchdog = ledgerWatchdog
        )
    }

    internal val ledgerProtocol: LedgerProtocol by lazy {
        LedgerProtocol(
            ledgerWatchdog = ledgerWatchdog,
            multicast = ioModule.multicastChannel,
            hostObservable = hostModule.selfHostInfoWatcher,
            messageSender = ledgerMessageSender,
            messageReceiver = ledgerMessageReceiver,
            ledgerDb = ledgerDb
        )
    }
}