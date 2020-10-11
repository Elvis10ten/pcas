package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.async.ThreadRunner
import com.fluentbuild.pcas.HostInfoObservable
import com.fluentbuild.pcas.io.SecureMulticastChannel
import com.fluentbuild.pcas.ledger.*
import com.fluentbuild.pcas.ledger.messages.MessageReceiver
import com.fluentbuild.pcas.ledger.messages.SimpleMessageSender
import com.fluentbuild.pcas.utils.JvmTimeProvider
import kotlinx.serialization.protobuf.ProtoBuf
import kotlin.random.Random

internal class LedgerModule(
	random: Random,
	timeProvider: JvmTimeProvider,
	threadRunner: () -> ThreadRunner,
	protoBuf: ProtoBuf,
	hostObservable: HostInfoObservable,
	multicast: SecureMulticastChannel,
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