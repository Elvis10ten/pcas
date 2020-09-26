package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.async.Watcher
import com.fluentbuild.pcas.host.PeripheralBondsWatcher
import com.fluentbuild.pcas.host.PeripheralConnector
import com.fluentbuild.pcas.host.audio.*
import com.fluentbuild.pcas.ledger.models.BondEntity
import com.fluentbuild.pcas.ledger.models.PropertyEntity
import com.fluentbuild.pcas.middleware.ServiceRegistry
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.services.audio.*
import com.fluentbuild.pcas.utils.Mapper

class AudioServiceModule(
    private val peripheral: Peripheral,
    private val androidModule: AndroidModule,
    private val utilsModule: UtilsModule
) {

    internal val serviceId = 1

    private val audioRouter by lazy {
        AndroidAudioRouter()
    }

    private val profileHolder: BluetoothProfileHolder by lazy {
        BluetoothProfileHolder(androidModule.appContext, androidModule.bluetoothAdapter)
    }

    private val a2dpConnector: PeripheralConnector by lazy {
        A2dpConnector(profileHolder, androidModule.bluetoothAdapter)
    }

    private val hspConnector: PeripheralConnector by lazy {
        HspConnector(profileHolder, androidModule.bluetoothAdapter)
    }

    internal val commandHandler: AudioCommandHandler by lazy {
        AudioCommandHandler(
            audioPeripheral = peripheral,
            a2dpConnector = a2dpConnector,
            hspConnector = hspConnector,
            audioRouter = audioRouter
        )
    }

    internal val propertyWatcher: Watcher<AudioProperty> by lazy {
        AudioPropertyWatcher(androidModule.telephonyManager, androidModule.audioManager)
    }

    internal val propertyEntityMapper: Mapper<AudioProperty, Set<PropertyEntity>> by lazy {
        PropertyEntityMapper(serviceId, utilsModule.timeProvider)
    }

    internal val bondsWatcher: PeripheralBondsWatcher by lazy {
        AudioBondsWatcher(androidModule.appContext, profileHolder)
    }

    internal val bondsEntityMapper: Mapper<Set<PeripheralBond>, Set<BondEntity>> by lazy {
        BondsEntityMapper(serviceId)
    }

    fun provideAudioStateUpdater(serviceRegistry: ServiceRegistry): AudioStateUpdater {
        return AudioStateUpdater(
            serviceRegistry = serviceRegistry,
            propertyWatcher = propertyWatcher,
            propertyEntityMapper = propertyEntityMapper,
            bondsWatcher = bondsWatcher,
            bondsEntityMapper = bondsEntityMapper
        )
    }
}