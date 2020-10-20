package com.fluentbuild.pcas

import android.content.Context
import android.media.projection.MediaProjection
import android.os.Handler
import com.fluentbuild.pcas.di.AsyncModuleAndroid
import com.fluentbuild.pcas.di.AudioServiceModule
import com.fluentbuild.pcas.di.ContentionModule
import com.fluentbuild.pcas.di.HostModule
import com.fluentbuild.pcas.di.IoModuleAndroid
import com.fluentbuild.pcas.di.LedgerModule
import com.fluentbuild.pcas.di.StreamModule
import com.fluentbuild.pcas.di.UtilsModuleAndroid
import com.fluentbuild.pcas.di.WatchersModule
import com.fluentbuild.pcas.host.HostConfig
import com.fluentbuild.pcas.io.AddressProviderAndroid
import kotlinx.serialization.protobuf.ProtoBuf

internal class EngineComponentAndroid(
	mediaProjection: MediaProjection?,
	appContext: Context,
	hostConfig: HostConfig,
	protoBuf: ProtoBuf
): EngineComponent {

	private val mainHandler = Handler(appContext.mainLooper)

	private val hostAddressProvider = AddressProviderAndroid(appContext)

	private val asyncModule = AsyncModuleAndroid(mainHandler)

	private val utilsModule = UtilsModuleAndroid()

	private val ioModule = IoModuleAndroid(
		appContext = appContext,
		hostConfig = hostConfig,
		threadRunnerProvider = asyncModule.threadRunnerProvider,
		secureRandom = utilsModule.secureRandom,
		hostAddressProvider = hostAddressProvider
	)

	private val watchersModule = WatchersModule(
		appContext = appContext,
		mainThreadHandler = mainHandler,
		hostAddressProvider = hostAddressProvider
	)

	private val hostModule = HostModule(
		appContext = appContext,
		hostConfig = hostConfig,
		unicastChannel = ioModule.unicastChannel,
		networkAddressWatcher = watchersModule.networkAddressWatcher,
		interactivityWatcher = watchersModule.interactivityWatcher,
		hostAddressProvider = hostAddressProvider
	)

	private val audioServiceModule = AudioServiceModule(
		appContext = appContext,
		mainHandler = mainHandler,
		hostConfig = hostConfig,
		hostObservable = hostModule.hostObservable,
		timeProvider = utilsModule.timeProvider,
		blockDebouncerProvider = asyncModule.debouncerProvider,
		callStateWatcher = watchersModule.callStateWatcher,
		audioPlaybackWatcher = watchersModule.audioPlaybackWatcher,
		profileStateWatcherProvider = watchersModule.profileStateWatcherProvider
	)

	private val ledgerModule = LedgerModule(
		random = utilsModule.random,
		timeProvider = utilsModule.timeProvider,
		threadRunnerProvider = asyncModule.threadRunnerProvider,
		protoBuf = protoBuf,
		hostObservable = hostModule.hostObservable,
		multicastChannel = ioModule.multicastChannel,
		audioBlocksObservable = audioServiceModule.audioBlocksProducer
	)

	private val streamModule = StreamModule(
		unicastChannel = ioModule.unicastChannel,
		audioStreamHandler = audioServiceModule.streamHandler,
		protoBuf = protoBuf
	)

	private val contentionModule = ContentionModule(
		timeProvider = utilsModule.timeProvider,
		audioResolutionHandler = audioServiceModule.resolutionHandler
	)

	override val ledgerProtocol = ledgerModule.ledgerProtocol

	override val streamDemuxer = streamModule.streamDemuxer

	override val contentionsResolver = contentionModule.contentionsResolver

	override fun release() {
		contentionsResolver.release()
		asyncModule.release()
	}
}