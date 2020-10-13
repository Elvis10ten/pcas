package com.fluentbuild.pcas

import android.content.Context
import android.os.Handler
import com.fluentbuild.pcas.host.HostConfigStore
import com.fluentbuild.pcas.logs.RichLog
import com.fluentbuild.pcas.peripheral.PeripheralRepositoryAndroid
import com.fluentbuild.pcas.utils.bluetoothAdapter
import kotlinx.serialization.protobuf.ProtoBuf
import timber.log.LogcatTree
import timber.log.Timber

class AppComponent(
	private val appContext: Context,
	isDebug: Boolean
) {

	val richLog = RichLog

	val mainHandler = Handler(appContext.mainLooper)

	private val protoBuf = ProtoBuf

	val hostConfigStore = HostConfigStore(protoBuf, appContext.filesDir) {
		appContext.bluetoothAdapter.name
	}

	val appStateObservable = AppStateObservable()

	val engine = Engine(appStateObservable) {
		EngineComponentAndroid(appContext, mainHandler, hostConfigStore.get(), protoBuf)
	}

	val peripheralRepository = PeripheralRepositoryAndroid(appContext)

	init {
		if(isDebug) {
			Timber.plant(LogcatTree())
		}
	}
}