package com.fluentbuild.pcas.host

import com.fluentbuild.pcas.EngineStateObservable
import com.fluentbuild.pcas.io.AtomicFile
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.services.ServiceClass
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

class HostConfigStore(
	private val protoBuf: ProtoBuf,
	private val atomicFile: AtomicFile,
	nameProvider: () -> String,
	randomUuidGenerator: () -> Uuid
) {

	private var cachedConfig: HostConfig? = null

	internal var engineStateObservable: EngineStateObservable? = null

	init {
		if(!atomicFile.exists()) {
			update(HostConfig(randomUuidGenerator(), nameProvider()))
		}

		engineStateObservable?.update(get())
	}

	fun setNetworkKey(newNetworkKey: ByteArray?) {
		get().apply {
			networkKey = newNetworkKey
			update(this)
		}
	}

	fun setPeripheral(serviceClass: ServiceClass, peripheral: Peripheral) {
		get().apply {
			peripherals[serviceClass] = peripheral
			update(this)
		}
	}

	fun setAudioCaptureEnabled(audioCaptureEnabled: Boolean) {
		get().apply {
			isAudioCaptureEnabled = audioCaptureEnabled
			update(this)
		}
	}

	private fun update(hostConfig: HostConfig) {
		cachedConfig = hostConfig
		atomicFile.write(protoBuf.encodeToByteArray(hostConfig))
		engineStateObservable?.update(hostConfig)
	}

	fun get(): HostConfig {
		val cachedConfig = cachedConfig
		if(cachedConfig != null) return cachedConfig

		val hostConfig = protoBuf.decodeFromByteArray<HostConfig>(atomicFile.readData())
		this.cachedConfig = hostConfig
		return hostConfig
	}

	companion object {

		const val FILE_NAME = "host_info.config"
	}
}