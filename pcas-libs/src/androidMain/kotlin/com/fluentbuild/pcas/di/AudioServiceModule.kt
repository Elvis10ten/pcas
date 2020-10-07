package com.fluentbuild.pcas.di

import android.content.Context
import android.os.Handler
import com.fluentbuild.pcas.values.Observable
import com.fluentbuild.pcas.host.HostInfoObservable
import com.fluentbuild.pcas.peripheral.PeripheralCommander
import com.fluentbuild.pcas.Engine
import com.fluentbuild.pcas.async.Debouncer
import com.fluentbuild.pcas.stream.StreamHandler
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.services.audio.*
import com.fluentbuild.pcas.utils.TimeProvider

internal class AudioServiceModule(
    appContext: Context,
    mainHandler: Handler,
    audioPeripheral: Peripheral,
    hostObservable: HostInfoObservable,
    timeProvider: TimeProvider,
    debouncer: () -> Debouncer
) {

    private val audioStreamer = AndroidAudioStreamer()

    private val profileHolder = BluetoothProfileHolder(appContext)

    private val a2dpConnector = A2dpCommander(appContext, profileHolder)

    private val hspConnector = HspCommander(appContext, profileHolder)

    private val propertyObservable = AudioPropertyObservable(appContext, mainHandler)

    private val blocksBuilder = AudioBlocksBuilder(audioPeripheral, timeProvider, hostObservable)

    private val bondsObservable = AndroidAudioBondsObservable(appContext, audioPeripheral, profileHolder)

    val resolutionHandler = AudioResolutionHandler(
        audioPeripheral = audioPeripheral,
        a2dpCommander = a2dpConnector,
        hspCommander = hspConnector,
        audioStreamer = audioStreamer
    )

    val streamHandler = AndroidAudioStreamHandler()

    val audioBlocksProducer = AudioBlocksProducer(
        audioPeripheral = audioPeripheral,
        propObservable = propertyObservable,
        bondsObservable = bondsObservable,
        timeProvider = timeProvider,
        hostObservable = hostObservable,
        debouncer = debouncer()
    )
}