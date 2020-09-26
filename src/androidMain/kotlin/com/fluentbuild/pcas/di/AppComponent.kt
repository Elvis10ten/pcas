package com.fluentbuild.pcas.di

import android.content.Context
import android.os.Handler
import com.fluentbuild.pcas.peripheral.Peripheral

class AppComponent(
    appContext: Context,
    mainThreadHandler: Handler,
    audioPeripheral: Peripheral
) {

    val androidModule = AndroidModule(appContext)

    val utilsModule = UtilsModule(mainThreadHandler)

    val ioModule = IoModule()

    val hostModule = HostModule()

    val ledgerModule = LedgerModule(ioModule, hostModule, utilsModule)

    val audioServiceModule = AudioServiceModule(audioPeripheral, androidModule, utilsModule)

    val servicesModule = ServicesModule(audioServiceModule)

    val middlewareModule = MiddlewareModule(servicesModule, ledgerModule)

}