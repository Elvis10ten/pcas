package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.async.ThreadRunner
import com.fluentbuild.pcas.host.HostInfoObservable
import com.fluentbuild.pcas.io.MulticastChannel
import com.fluentbuild.pcas.ledger.*
import com.fluentbuild.pcas.ledger.messages.MessageReceiver
import com.fluentbuild.pcas.ledger.messages.SimpleMessageSender
import com.fluentbuild.pcas.utils.TimeProvider
import kotlinx.serialization.protobuf.ProtoBuf
import kotlin.random.Random

internal class LedgerModule(
    random: Random,
    timeProvider: TimeProvider,
    threadRunner: () -> ThreadRunner,
    protoBuf: ProtoBuf,
    hostObservable: HostInfoObservable,
    multicast: MulticastChannel,
    serviceBlocksProducers: List<BlocksProducer>
) {

    private val ledgerDb = LedgerDb()

    private val ledgerMessageSender = SimpleMessageSender(
        protoBuf = protoBuf,
        ledgerDb = ledgerDb,
        runner = threadRunner(),
        multicast = multicast,
        random = random
    )

    private val ledgerWatchdog = LedgerWatchdog(
        runner = threadRunner(),
        ledgerDb = ledgerDb,
        messageSender = ledgerMessageSender,
        timeProvider = timeProvider,
    )

    private val ledgerMessageReceiver = MessageReceiver(
        protoBuf = protoBuf,
        messageSender = ledgerMessageSender,
        ledgerDb = ledgerDb,
        watchdog = ledgerWatchdog
    )

    internal val ledgerProtocol = LedgerProtocol(
        ledgerWatchdog = ledgerWatchdog,
        multicast = multicast,
        hostObservable = hostObservable,
        messageSender = ledgerMessageSender,
        messageReceiver = ledgerMessageReceiver,
        ledgerDb = ledgerDb,
        serviceBlocksProducers = serviceBlocksProducers
    )
}