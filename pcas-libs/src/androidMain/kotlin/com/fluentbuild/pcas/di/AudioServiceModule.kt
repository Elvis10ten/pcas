package com.fluentbuild.pcas.di

import android.bluetooth.BluetoothProfile
import android.content.Context
import com.fluentbuild.pcas.host.HostConfig
import com.fluentbuild.pcas.bluetooth.BluetoothProfileHolder
import com.fluentbuild.pcas.host.HostInfoObservable
import com.fluentbuild.pcas.async.Debouncer
import com.fluentbuild.pcas.bluetooth.BluetoothPeripheralCommander
import com.fluentbuild.pcas.services.audio.*
import com.fluentbuild.pcas.utils.TimeProviderJvm
import com.fluentbuild.pcas.watchers.AudioPlaybackWatcher
import com.fluentbuild.pcas.watchers.BluetoothProfileStateWatcher
import com.fluentbuild.pcas.watchers.CallStateWatcher

internal class AudioServiceModule(
	appContext: Context,
	hostConfig: HostConfig,
	hostObservable: HostInfoObservable,
	timeProvider: TimeProviderJvm,
	blockBuilderDebouncer: (Int) -> Debouncer,
	callStateWatcher: CallStateWatcher,
	audioPlaybackWatcher: AudioPlaybackWatcher,
	a2dpProfileStateWatcher: BluetoothProfileStateWatcher,
	hspProfileStateWatcher: BluetoothProfileStateWatcher
) {

    private val audioStreamer = AudioStreamerAndroid()

    private val audioProfileHolder = BluetoothProfileHolder(appContext)

    private val a2dpCommander = BluetoothPeripheralCommander(appContext, audioProfileHolder, BluetoothProfile.A2DP)

    private val hspCommander = BluetoothPeripheralCommander(appContext, audioProfileHolder, BluetoothProfile.HEADSET)

    private val propertyObservable = AudioPropertyObservable(appContext, callStateWatcher, audioPlaybackWatcher)

    private val bondsObservable = AudioBondsObservableAndroid(
		context = appContext,
		audioPeripheral = hostConfig.audioPeripheral,
		profileHolder = audioProfileHolder,
		a2dpProfileStateWatcher = a2dpProfileStateWatcher,
		hspProfileStateWatcher = hspProfileStateWatcher
	)

	private val audioBlocksBuilderProvider = { AudioBlocksBuilder(hostConfig, timeProvider, hostObservable) }

	val streamHandler = AudioStreamHandlerAndroid()

	val resolutionHandler = AudioResolutionHandler(
        audioPeripheral = hostConfig.audioPeripheral,
        a2dpCommander = a2dpCommander,
        hspCommander = hspCommander,
        audioStreamer = audioStreamer
    )

    val audioBlocksProducer = AudioBlocksProducer(
        propObservable = propertyObservable,
        bondsObservable = bondsObservable,
        debouncer = blockBuilderDebouncer(AUDIO_BLOCK_DEBOUNCE_DELAY_MILLIS),
		audioBlocksBuilderProvider = audioBlocksBuilderProvider
    )

	companion object {

		private const val AUDIO_BLOCK_DEBOUNCE_DELAY_MILLIS = 100
	}
}