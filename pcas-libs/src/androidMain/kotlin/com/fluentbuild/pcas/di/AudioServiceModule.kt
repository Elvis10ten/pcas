package com.fluentbuild.pcas.di

import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothHeadset
import android.bluetooth.BluetoothProfile
import android.content.Context
import com.fluentbuild.pcas.host.HostConfig
import com.fluentbuild.pcas.bluetooth.BluetoothProfileHolder
import com.fluentbuild.pcas.host.HostInfoObservable
import com.fluentbuild.pcas.async.Debouncer
import com.fluentbuild.pcas.bluetooth.BluetoothPeripheralCommander
import com.fluentbuild.pcas.peripheral.PeripheralProfile
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
	blockDebouncerProvider: (Int) -> Debouncer,
	callStateWatcher: CallStateWatcher,
	audioPlaybackWatcher: AudioPlaybackWatcher,
	profileStateWatcherProvider: (stateChangedAction: String) -> BluetoothProfileStateWatcher
) {

    private val audioStreamer = AudioStreamerAndroid()

    private val audioProfileHolder = BluetoothProfileHolder(appContext)

    private val a2dpCommander = BluetoothPeripheralCommander(appContext, audioProfileHolder, BluetoothProfile.A2DP)

    private val headsetCommander = BluetoothPeripheralCommander(appContext, audioProfileHolder, BluetoothProfile.HEADSET)

    private val propertyObservable = AudioPropertyObservable(appContext, callStateWatcher, audioPlaybackWatcher)

	private val profileStateWatchers = mapOf(
		PeripheralProfile.A2DP to profileStateWatcherProvider(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED),
		PeripheralProfile.HEADSET to profileStateWatcherProvider(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED),
	)

    private val bondsObservable = AudioBondsObservableAndroid(
		context = appContext,
		audioPeripheral = hostConfig.audioPeripheral!!,
		profileHolder = audioProfileHolder,
		profileStateWatchers = profileStateWatchers
	)

	private val audioBlocksBuilderProvider = {
		AudioBlocksBuilder(
			audioPeripheral = hostConfig.audioPeripheral!!,
			canCaptureAudio = hostConfig.canCaptureAudio,
			timeProvider = timeProvider,
			hostObservable = hostObservable
		)
	}

	val streamHandler = AudioStreamHandlerAndroid()

	val resolutionHandler = AudioResolutionHandler(
        audioPeripheral = hostConfig.audioPeripheral!!,
        a2dpCommander = a2dpCommander,
        hspCommander = headsetCommander,
        audioStreamer = audioStreamer
    )

    val audioBlocksProducer = AudioBlocksObservable(
        propObservable = propertyObservable,
        bondsObservable = bondsObservable,
        debouncer = blockDebouncerProvider(AUDIO_BLOCK_DEBOUNCE_DELAY_MILLIS),
		blocksBuilderProvider = audioBlocksBuilderProvider
    )

	companion object {

		private const val AUDIO_BLOCK_DEBOUNCE_DELAY_MILLIS = 100
	}
}