package com.fluentbuild.pcas.di

import android.content.Context
import android.os.Handler
import com.fluentbuild.pcas.HostConfig
import com.fluentbuild.pcas.bluetooth.BluetoothProfileHolder
import com.fluentbuild.pcas.HostInfoObservable
import com.fluentbuild.pcas.async.Debouncer
import com.fluentbuild.pcas.services.audio.*
import com.fluentbuild.pcas.utils.JvmTimeProvider

internal class AudioServiceModule(
	appContext: Context,
	mainHandler: Handler,
	hostConfig: HostConfig,
	hostObservable: HostInfoObservable,
	timeProvider: JvmTimeProvider,
	debouncerProvider: () -> Debouncer
) {

    private val audioStreamer = AndroidAudioStreamer()

    private val bluetoothProfileHolder = BluetoothProfileHolder(appContext)

    private val a2dpCommander = A2dpCommander(appContext, bluetoothProfileHolder)

    private val hspCommander = HspCommander(appContext, bluetoothProfileHolder)

    private val propertyObservable = AudioPropertyObservable(appContext, mainHandler)

    private val bondsObservable = AndroidAudioBondsObservable(
		context = appContext,
		audioPeripheral = hostConfig.audioPeripheral,
		profileHolder = bluetoothProfileHolder
	)

	private val audioBlocksBuilderProvider = { AudioBlocksBuilder(hostConfig, timeProvider, hostObservable) }

	val streamHandler = AndroidAudioStreamHandler()

	val resolutionHandler = AudioResolutionHandler(
        audioPeripheral = hostConfig.audioPeripheral,
        a2dpCommander = a2dpCommander,
        hspCommander = hspCommander,
        audioStreamer = audioStreamer
    )

    val audioBlocksProducer = AudioBlocksProducer(
        propObservable = propertyObservable,
        bondsObservable = bondsObservable,
        debouncer = debouncerProvider(),
		audioBlocksBuilderProvider = audioBlocksBuilderProvider
    )
}