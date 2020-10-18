package com.fluentbuild.pcas

import android.content.Context
import com.fluentbuild.pcas.host.HostConfigStore
import com.fluentbuild.pcas.io.AtomicFileJvm
import com.fluentbuild.pcas.peripheral.PeripheralRepositoryAndroid
import com.fluentbuild.pcas.utils.bluetoothAdapter
import com.fluentbuild.pcas.utils.createRandomUuid
import kotlinx.serialization.protobuf.ProtoBuf
import timber.log.LogcatTree
import timber.log.Timber
import java.io.File

class AppComponent(
	private val appContext: Context,
	isDebug: Boolean
) {

	private val protoBuf = ProtoBuf

	val engineStateObservable = EngineStateObservable()

	val hostConfigStore = HostConfigStore(
		engineStateObservable = engineStateObservable,
		protoBuf = protoBuf,
		atomicFile = AtomicFileJvm(File(appContext.filesDir, HostConfigStore.FILE_NAME)),
		nameProvider = { appContext.bluetoothAdapter.name },
		randomUuidGenerator = { createRandomUuid() }
	)

	val engine = Engine(engineStateObservable) {
		EngineComponentAndroid(
			appContext = appContext,
			hostConfig = hostConfigStore.get(),
			protoBuf = protoBuf
		)
	}

	val peripheralRepository = PeripheralRepositoryAndroid(appContext)

	init {
		if(isDebug) {
			Timber.plant(LogcatTree())
		}
	}
}