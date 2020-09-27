package com.fluentbuild.pcas.di

import android.content.Context
import android.os.Handler
import com.fluentbuild.pcas.peripheral.Peripheral
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

    private val audioServiceModule = AudioServiceModule(appContext, audioPeripheral, utilsModule)

    private val servicesModule = ServicesModule(audioServiceModule)

    private val middlewareModule = MiddlewareModule(servicesModule, ledgerModule)

    fun init() {
        audioServiceModule.init(middlewareModule.serviceRegistry)
    }

    fun release() {
        asyncModule.threadPool.shutdownNow()
    }
}