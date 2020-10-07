package com.fluentbuild.pcas.di

import android.content.Context
import com.fluentbuild.pcas.values.Observable
import com.fluentbuild.pcas.host.HostInfoObservable
import com.fluentbuild.pcas.services.audio.AudioBondsObservable
import com.fluentbuild.pcas.peripheral.PeripheralCommander
import com.fluentbuild.pcas.Engine
import com.fluentbuild.pcas.stream.StreamHandler
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.services.audio.*
import com.fluentbuild.pcas.utils.TimeProvider

internal class AudioServiceModule(
    private val appContext: Context,
    private val audioPeripheral: Peripheral,
    private val hostInfoObservable: () -> HostInfoObservable,
    private val timeProvider: TimeProvider
) {

    lateinit var audioBlocksProducer: AudioBlocksProducer

    private val audioStreamer: AudioStreamer by lazy {
        AndroidAudioStreamer()
    }

    private val profileHolder: BluetoothProfileHolder by lazy {
        BluetoothProfileHolder(appContext)
    }

    private val a2dpConnector: PeripheralCommander by lazy {
        A2dpConnector(appContext, profileHolder)
    }

    private val hspConnector: PeripheralCommander by lazy {
        HspConnector(appContext, profileHolder)
    }

    private val propertyObservable: Observable<AudioProperty> by lazy {
        AudioPropertyObservable(appContext)
    }

    private val blocksBuilder: AudioBlocksBuilder by lazy {
        AudioBlocksBuilder(timeProvider, hostInfoObservable())
    }

    private val bondsObservable: AudioBondsObservable by lazy {
        AndroidAudioBondsObservable(appContext, audioPeripheral, profileHolder)
    }

    val resolutionHandler: AudioResolutionHandler by lazy {
        AudioResolutionHandler(
            audioPeripheral = audioPeripheral,
            a2dpConnector = a2dpConnector,
            hspConnector = hspConnector,
            audioStreamer = audioStreamer
        )
    }

    val streamHandler: StreamHandler by lazy {
        AndroidAudioStreamHandler()
    }

    fun init(engine: Engine) {
        audioBlocksProducer = AudioBlocksProducer(
            serviceRegistry = engine,
            propertyObservable = propertyObservable,
            bondsObservable = bondsObservable,
            blocksBuilder = blocksBuilder
        )
    }
}