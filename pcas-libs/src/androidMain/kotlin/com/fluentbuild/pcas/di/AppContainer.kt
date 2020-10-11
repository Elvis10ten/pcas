package com.fluentbuild.pcas.di

import android.content.Context
import android.os.Handler
import com.fluentbuild.pcas.HostInfoObservableAndroid
import com.fluentbuild.pcas.Engine
import com.fluentbuild.pcas.HostConfig
import com.fluentbuild.pcas.io.AndroidAddressProvider
import com.fluentbuild.pcas.logs.getLog
import com.fluentbuild.pcas.services.audio.AudioConfig
import timber.log.LogcatTree
import timber.log.Timber

/**
 * The root container for all PCAS dependencies.
 *
 * Doing manual DI as Koin/Kodeine is roughly the same amount of work with no compile time guarantees :)
 *
 * This should be revisited when a better KMP DI tool is found.
 */
class AppContainer(
    appContext: Context,
    hostConfig: HostConfig
) {

    private val log = getLog()

    private val mainThreadHandler = Handler(appContext.mainLooper)

    private val hostAddressProvider = AndroidAddressProvider(appContext)

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
        mainHandler = mainThreadHandler,
        hostConfig = hostConfig,
        hostObservable = hostObservable,
        timeProvider = utilsModule.timeProvider,
        debouncerProvider = asyncModule.debouncerProvider
    )

    private val ledgerModule = LedgerModule(
        random = utilsModule.random,
        timeProvider = utilsModule.timeProvider,
        threadRunnerProvider = asyncModule.threadRunnerProvider,
        protoBuf = utilsModule.protoBuf,
        hostObservable = hostObservable,
        multicastChannel = ioModule.multicastChannel,
        audioBlocksProducer = audioServiceModule.audioBlocksProducer
    )

    private val streamModule = StreamModule(
        unicastChannel = ioModule.unicastChannel,
        audioStreamHandler = audioServiceModule.streamHandler
    )

    private val contentionModule = ContentionModule(
        timeProvider = utilsModule.timeProvider,
        audioResolutionHandler = audioServiceModule.resolutionHandler
    )

    val engine = Engine(
        ledgerProtocol = ledgerModule.ledgerProtocol,
        streamDemux = streamModule.streamDemux,
        contentionsResolver = contentionModule.contentionsResolver
    )

    fun init(debug: Boolean) {
        log.info { "Initializing AppContainer" }
        if(debug) {
            Timber.plant(LogcatTree())
        }
    }

    fun release() {
        log.info { "Releasing AppContainer" }
        asyncModule.threadPool.shutdownNow()
    }
}