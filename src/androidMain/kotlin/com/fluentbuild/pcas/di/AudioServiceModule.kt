package com.fluentbuild.pcas.di

import android.content.Context
import com.fluentbuild.pcas.async.Watcher
import com.fluentbuild.pcas.services.audio.PeripheralBondsWatcher
import com.fluentbuild.pcas.services.audio.PeripheralConnector
import com.fluentbuild.pcas.ledger.models.BondEntity
import com.fluentbuild.pcas.ledger.models.PropertyEntity
import com.fluentbuild.pcas.middleware.ServiceRegistry
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.services.audio.*
import com.fluentbuild.pcas.utils.Mapper

class AudioServiceModule(
    private val appContext: Context,
    private val peripheral: Peripheral,
    private val utilsModule: UtilsModule
) {

    val serviceId = 1

    lateinit var audioStateUpdater: AudioStateUpdater

    private val audioRouter: AudioRouter by lazy {
        AndroidAudioRouter()
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

    private val propertyWatcher: Watcher<AudioProperty> by lazy {
        AudioPropertyWatcher(appContext)
    }

    private val propertyEntityMapper: Mapper<AudioProperty, Set<PropertyEntity>> by lazy {
        PropertyEntityMapper(serviceId, utilsModule.timeProvider)
    }

    private val bondsWatcher: PeripheralBondsWatcher by lazy {
        AudioBondsWatcher(appContext, profileHolder)
    }

    private val bondsEntityMapper: Mapper<Set<PeripheralBond>, Set<BondEntity>> by lazy {
        BondsEntityMapper(serviceId)
    }

    val commandHandler: AudioCommandHandler by lazy {
        AudioCommandHandler(
            audioPeripheral = peripheral,
            a2dpConnector = a2dpConnector,
            hspConnector = hspConnector,
            audioRouter = audioRouter
        )
    }

    fun init(serviceRegistry: ServiceRegistry) {
        audioStateUpdater = AudioStateUpdater(
            serviceRegistry = serviceRegistry,
            propertyWatcher = propertyWatcher,
            propertyEntityMapper = propertyEntityMapper,
            bondsWatcher = bondsWatcher,
            bondsEntityMapper = bondsEntityMapper
        )
    }
}