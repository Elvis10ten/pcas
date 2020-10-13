package com.fluentbuild.pcas

import android.content.Context
import android.os.Handler
import com.fluentbuild.pcas.di.AsyncModuleAndroid
import com.fluentbuild.pcas.di.AudioServiceModule
import com.fluentbuild.pcas.di.ContentionModule
import com.fluentbuild.pcas.di.IoModuleAndroid
import com.fluentbuild.pcas.di.LedgerModule
import com.fluentbuild.pcas.di.StreamModule
import com.fluentbuild.pcas.di.UtilsModuleAndroid
import com.fluentbuild.pcas.di.WatchersModule
import com.fluentbuild.pcas.host.HostConfig
import com.fluentbuild.pcas.host.HostInfoObservableAndroid
import com.fluentbuild.pcas.io.AddressProviderAndroid
import com.fluentbuild.pcas.services.audio.AudioConfig
import kotlinx.serialization.protobuf.ProtoBuf

internal class EngineComponentAndroid(
	appContext: Context,
	mainThreadHandler: Handler,
	hostConfig: HostConfig,
	protoBuf: ProtoBuf
): EngineComponent {

	private val hostAddressProvider = AddressProviderAndroid(appContext)

	private val asyncModule = AsyncModuleAndroid(mainThreadHandler)

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
		mainThreadHandler = mainThreadHandler,
		hostAddressProvider = hostAddressProvider
	)

	private val hostObservable = HostInfoObservableAndroid(
		context = appContext,
		hostConfig = hostConfig,
		hostAddressProvider = hostAddressProvider,
		unicastChannel = ioModule.unicastChannel,
		audioConfig = AudioConfig,
		networkAddressWatcher = watchersModule.networkAddressWatcher,
		interactivityWatcher = watchersModule.interactivityWatcher
	)

	private val audioServiceModule = AudioServiceModule(
		appContext = appContext,
		hostConfig = hostConfig,
		hostObservable = hostObservable,
		timeProvider = utilsModule.timeProvider,
		debouncerProvider = asyncModule.debouncerProvider,
		callStateWatcher = watchersModule.callStateWatcher,
		audioPlaybackWatcher = watchersModule.audioPlaybackWatcher,
		a2dpProfileStateWatcher = watchersModule.a2dpProfileStateWatcher,
		hspProfileStateWatcher = watchersModule.hspProfileStateWatcher
	)

	private val ledgerModule = LedgerModule(
		random = utilsModule.random,
		timeProvider = utilsModule.timeProvider,
		threadRunnerProvider = asyncModule.threadRunnerProvider,
		protoBuf = protoBuf,
		hostObservable = hostObservable,
		multicastChannel = ioModule.multicastChannel,
		audioBlocksProducer = audioServiceModule.audioBlocksProducer
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
		asyncModule.threadPool.shutdownNow()
	}
}