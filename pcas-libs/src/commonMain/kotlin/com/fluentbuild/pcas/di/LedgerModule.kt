package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.async.ThreadRunner
import com.fluentbuild.pcas.HostInfoObservable
import com.fluentbuild.pcas.io.SecureMulticastChannel
import com.fluentbuild.pcas.ledger.*
import com.fluentbuild.pcas.ledger.messages.MessageReceiver
import com.fluentbuild.pcas.ledger.messages.SimpleMessageSender
import com.fluentbuild.pcas.utils.TimeProvider
import kotlinx.serialization.protobuf.ProtoBuf
import kotlin.random.Random

internal class LedgerModule(
    random: Random,
    timeProvider: TimeProvider,
    threadRunnerProvider: () -> ThreadRunner,
    protoBuf: ProtoBuf,
    hostObservable: HostInfoObservable,
    multicastChannel: SecureMulticastChannel,
    audioBlocksProducer: BlocksProducer
) {

    private val ledgerDb = LedgerDb()

    private val serviceBlocksProducers = listOf(
        audioBlocksProducer
    )

    private val messageSender = SimpleMessageSender(
        protoBuf = protoBuf,
        ledgerDb = ledgerDb,
        runner = threadRunnerProvider(),
        multicast = multicastChannel,
        random = random
    )

    private val ledgerWatchdog = LedgerWatchdog(
        runner = threadRunnerProvider(),
        ledgerDb = ledgerDb,
        messageSender = messageSender,
        timeProvider = timeProvider,
    )

    private val messageReceiver = MessageReceiver(
        protoBuf = protoBuf,
        messageSender = messageSender,
        ledgerDb = ledgerDb,
        watchdog = ledgerWatchdog
    )

    internal val ledgerProtocol = LedgerProtocol(
        ledgerWatchdog = ledgerWatchdog,
        multicast = multicastChannel,
        hostObservable = hostObservable,
        messageSender = messageSender,
        messageReceiver = messageReceiver,
        ledgerDb = ledgerDb,
        serviceBlocksProducers = serviceBlocksProducers
    )
}