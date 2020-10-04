package com.fluentbuild.pcas.di

import android.content.Context
import com.fluentbuild.pcas.async.Observable
import com.fluentbuild.pcas.services.audio.PeripheralBondsObservable
import com.fluentbuild.pcas.services.audio.PeripheralConnector
import com.fluentbuild.pcas.ledger.models.BondEntity
import com.fluentbuild.pcas.ledger.models.PropertyEntity
import com.fluentbuild.pcas.middleware.ServiceRegistry
import com.fluentbuild.pcas.routing.RouterServer
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.services.audio.*
import com.fluentbuild.pcas.services.Mapper

class AudioServiceModule(
    private val appContext: Context,
    private val audioPeripheral: Peripheral,
    private val utilsModule: UtilsModule
) {

    val serviceId = 1

    lateinit var audioStateUpdater: AudioStateUpdater

    private val audioRouterClient: AudioRouterClient by lazy {
        AndroidAudioRouterClient()
    }

    private val profileHolder: BluetoothProfileHolder by lazy {
        BluetoothProfileHolder(appContext)
    }

    private val a2dpConnector: PeripheralConnector by lazy {
        A2dpConnector(appContext, profileHolder)
    }

    private val hspConnector: PeripheralConnector by lazy {
        HspConnector(appContext, profileHolder)
    }

    private val propertyObservable: Observable<AudioProperty> by lazy {
        AudioPropertyObservable(appContext)
    }

    private val propertyEntityMapper: Mapper<AudioProperty, Set<PropertyEntity>> by lazy {
        PropertyEntityMapper(serviceId, utilsModule.timeProvider)
    }

    private val bondsObservable: PeripheralBondsObservable by lazy {
        AudioBondsObservable(appContext, audioPeripheral, profileHolder)
    }

    private val bondsEntityMapper: Mapper<Set<PeripheralBond>, Set<BondEntity>> by lazy {
        BondsEntityMapper(serviceId)
    }

    val commandHandler: AudioCommandHandler by lazy {
        AudioCommandHandler(
            audioPeripheral = audioPeripheral,
            a2dpConnector = a2dpConnector,
            hspConnector = hspConnector,
            audioRouterClient = audioRouterClient
        )
    }

    val routerServer: RouterServer by lazy {
        AndroidAudioRouterServer()
    }

    fun init(serviceRegistry: ServiceRegistry) {
        audioStateUpdater = AudioStateUpdater(
            serviceRegistry = serviceRegistry,
            propertyObservable = propertyObservable,
            propertyEntityMapper = propertyEntityMapper,
            bondsObservable = bondsObservable,
            bondsEntityMapper = bondsEntityMapper
        )
    }
}