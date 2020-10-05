package com.fluentbuild.pcas.di

import android.content.Context
import android.os.Handler
import com.fluentbuild.pcas.Pcas
import com.fluentbuild.pcas.logs.ConsolePublisher
import com.fluentbuild.pcas.peripheral.Peripheral
import timber.log.LogcatTree
import timber.log.Timber
import javax.crypto.SecretKey

class AppComponent(
    appContext: Context,
    audioPeripheral: Peripheral,
    hostUuid: String,
    hostName: String,
    payloadKey: SecretKey
) {

    private val mainThreadHandler = Handler(appContext.mainLooper)

    private val asyncModule = AsyncModule(mainThreadHandler)

    private val utilsModule = UtilsModule()

    private val ioModule = IoModule(
        appContext,
        payloadKey,
        utilsModule.secureRandom,
        { asyncModule.provideThreadExecutor() }
    )

    private val hostModule = HostModule(appContext, hostUuid, hostName, ioModule)

    private val ledgerModule = LedgerModule(ioModule, hostModule, utilsModule, asyncModule)

    private val audioServiceModule = AudioServiceModule(
        appContext,
        audioPeripheral,
        { hostModule.selfHostInfoWatcher },
        utilsModule.timeProvider
    )

    private val servicesModule = ServicesModule(audioServiceModule)

    private val middlewareModule = MiddlewareModule(
        ioModule,
        servicesModule,
        ledgerModule
    )

    val pcas by lazy {
        Pcas(
            middlewareModule.serviceRegistry,
            audioServiceModule.audioStateUpdater
        )
    }

    fun init(debug: Boolean) {
        ConsolePublisher.isEnabled = debug
        if(debug) {
            Timber.plant(LogcatTree())
        }

        audioServiceModule.init(middlewareModule.serviceRegistry)
    }

    fun release() {
        asyncModule.threadPool.shutdownNow()
    }
}