package com.fluentbuild.pcas

import android.content.Context
import android.media.projection.MediaProjection
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

	var mediaProjection: MediaProjection? = null
		private set(value) {
			field = value
			hostConfigStore.setAudioCaptureEnabled(value != null)
		}

	val hostConfigStore = HostConfigStore(
		protoBuf = protoBuf,
		atomicFile = AtomicFileJvm(File(appContext.filesDir, HostConfigStore.FILE_NAME)),
		nameProvider = { appContext.bluetoothAdapter.name },
		randomUuidGenerator = { createRandomUuid() }
	)

	val engineStateObservable = EngineStateObservable(hostConfigStore)

	val engine = Engine(engineStateObservable) {
		EngineComponentAndroid(
			mediaProjection = mediaProjection,
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

		hostConfigStore.engineStateObservable = engineStateObservable
	}
}