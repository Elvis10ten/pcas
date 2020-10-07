package com.fluentbuild.pcas.di

import android.content.Context
import android.os.Handler
import com.fluentbuild.pcas.Engine
import com.fluentbuild.pcas.host.Uuid
import com.fluentbuild.pcas.peripheral.Peripheral
import timber.log.LogcatTree
import timber.log.Timber
import javax.crypto.SecretKey

class AppComponent(
    appContext: Context,
    audioPeripheral: Peripheral,
    hostUuid: Uuid,
    hostName: String,
    networkKey: SecretKey
) {

    private val mainThreadHandler = Handler(appContext.mainLooper)

    private val asyncModule = AsyncModule(mainThreadHandler)

    private val utilsModule = UtilsModule()

    private val ioModule = IoModule(
        appContext,
        networkKey,
        { asyncModule.provideThreadExecutor() },
        utilsModule.secureRandom
    )

    private val hostModule = HostModule(
        appContext,
        mainThreadHandler,
        hostUuid,
        hostName,
        ioModule
    )

    private val audioServiceModule = AudioServiceModule(
        appContext,
        mainThreadHandler,
        audioPeripheral,
        hostModule.selfHostInfoWatcher,
        utilsModule.timeProvider,
        { asyncModule.provideDebouncer() }
    )

    private val ledgerModule = LedgerModule(
        utilsModule.timeProvider,
        { asyncModule.provideThreadExecutor() },
        utilsModule.protoBuf,
        hostModule.selfHostInfoWatcher,
        ioModule.multicastChannel,
        listOf(
            audioServiceModule.audioBlocksProducer
        )
    )

    private val streamModule = StreamModule(
        ioModule.unicastChannel,
        audioServiceModule.streamHandler
    )

    private val conflictModule = ConflictsModule(
        utilsModule.timeProvider,
        audioServiceModule.resolutionHandler,
        { asyncModule.provideDebouncer() }
    )

    val engine: Engine by lazy {
        Engine(
            ledgerProtocol = ledgerModule.ledgerProtocol,
            streamDemux = streamModule.streamDemux,
            conflictsResolver = conflictModule.conflictsResolver
        )
    }

    fun init(debug: Boolean) {
        if(debug) {
            Timber.plant(LogcatTree())
        }
    }

    fun release() {
        asyncModule.threadPool.shutdownNow()
    }
}