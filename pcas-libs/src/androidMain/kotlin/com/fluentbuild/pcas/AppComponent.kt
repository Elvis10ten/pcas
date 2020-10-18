package com.fluentbuild.pcas

import android.content.Context
import com.fluentbuild.pcas.host.HostConfigStore
import com.fluentbuild.pcas.peripheral.PeripheralRepositoryAndroid
import com.fluentbuild.pcas.utils.bluetoothAdapter
import kotlinx.serialization.protobuf.ProtoBuf
import timber.log.LogcatTree
import timber.log.Timber

class AppComponent(
	private val appContext: Context,
	isDebug: Boolean
) {

	private val protoBuf = ProtoBuf

	val engineStateObservable = EngineStateObservable()

	val hostConfigStore = HostConfigStore(engineStateObservable, protoBuf, appContext.filesDir) {
		appContext.bluetoothAdapter.name
	}

	val engine = Engine(engineStateObservable) {
		EngineComponentAndroid(appContext, hostConfigStore.get(), protoBuf)
	}

	val peripheralRepository = PeripheralRepositoryAndroid(appContext)

	init {
		if(isDebug) {
			Timber.plant(LogcatTree())
		}
	}
}