package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.async.ThreadRunner
import com.fluentbuild.pcas.host.HostInfoObservable
import com.fluentbuild.pcas.io.MulticastChannel
import com.fluentbuild.pcas.ledger.*
import com.fluentbuild.pcas.ledger.messages.MessageReceiver
import com.fluentbuild.pcas.ledger.messages.SimpleMessageSender
import com.fluentbuild.pcas.utils.TimeProvider
import com.fluentbuild.pcas.values.Observable
import kotlinx.serialization.protobuf.ProtoBuf
import kotlin.random.Random

internal class LedgerModule(
    random: Random,
    timeProvider: TimeProvider,
    threadRunnerProvider: () -> ThreadRunner,
    protoBuf: ProtoBuf,
    hostObservable: HostInfoObservable,
    multicastChannel: MulticastChannel,
    audioBlocksObservable: Observable<Set<Block>>
) {

    private val ledgerDb = LedgerDb()

    private val serviceBlocksObservables = listOf(
        audioBlocksObservable
    )

    private val messageSender = SimpleMessageSender(
        protoBuf = protoBuf,
        ledgerDb = ledgerDb,
        runner = threadRunnerProvider(),
        multicast = multicastChannel,
        random = random
    )

    private val watchdog = LedgerWatchdog(
        runner = threadRunnerProvider(),
        ledgerDb = ledgerDb,
        messageSender = messageSender,
        timeProvider = timeProvider,
    )

    private val messageReceiver = MessageReceiver(
        protoBuf = protoBuf,
        messageSender = messageSender,
        ledgerDb = ledgerDb,
        watchdog = watchdog
    )

    internal val ledgerProtocol = LedgerProtocol(
        ledgerWatchdog = watchdog,
        multicastChannel = multicastChannel,
        hostObservable = hostObservable,
        messageSender = messageSender,
        messageReceiver = messageReceiver,
        ledgerDb = ledgerDb,
        serviceBlocksObservables = serviceBlocksObservables
    )
}