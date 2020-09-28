package com.fluentbuild.pcas.di

import android.content.Context
import android.os.Handler
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

    private val ioModule = IoModule(appContext, payloadKey, asyncModule)

    private val hostModule = HostModule(appContext, hostUuid, hostName, ioModule)

    private val ledgerModule = LedgerModule(ioModule, hostModule, utilsModule, asyncModule)

    val audioServiceModule = AudioServiceModule(appContext, audioPeripheral, utilsModule)

    private val servicesModule = ServicesModule(audioServiceModule)

    val middlewareModule = MiddlewareModule(ioModule, servicesModule, ledgerModule, utilsModule)

    fun init() {
        Timber.plant(LogcatTree())
        audioServiceModule.init(middlewareModule.serviceRegistry)
    }

    fun release() {
        asyncModule.threadPool.shutdownNow()
    }
}