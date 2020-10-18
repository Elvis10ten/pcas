package com.fluentbuild.pcas.host

import com.fluentbuild.pcas.EngineStateObservable
import com.fluentbuild.pcas.io.KeyTool
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.utils.AtomicFile
import com.fluentbuild.pcas.utils.createRandomUuid
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.File

class HostConfigStore(
	private val engineStateObservable: EngineStateObservable,
	private val protoBuf: ProtoBuf,
	privateFilesDir: File,
	private val nameProvider: () -> String
) {

	// have memory cache
	private val atomicFile = AtomicFile(File(privateFilesDir, HOST_CONFIG_FILE_NAME))

	fun setNetworkKey(networkKey: ByteArray) {
		val hostConfig = getInternal().copy(networkKey = networkKey)
		update(hostConfig)
	}

	fun setAudioPeripheral(audioPeripheral: Peripheral) {
		val hostConfig = getInternal().copy(audioPeripheral = audioPeripheral)
		update(hostConfig)
	}

	private fun update(hostConfig: HostConfigPartial) {
		atomicFile.startWrite().apply {
			write(protoBuf.encodeToByteArray(hostConfig))
			atomicFile.finishWrite(this)
		}
	}

	fun get(): HostConfig {
		return getInternal().createFull()
	}

	fun isPeripheralsSetup(): Boolean {
		return  getPartial().audioPeripheral != null
	}

	fun getPartial(): HostConfigPartial {
		return getInternal()
	}

	private fun getInternal(): HostConfigPartial {
		return try {
			protoBuf.decodeFromByteArray(atomicFile.openRead().readBytes())
		} catch (e: Exception) {
			HostConfigPartial(createRandomUuid(), nameProvider(), KeyTool.generate().encoded)
		}
	}

	companion object {

		private const val HOST_CONFIG_FILE_NAME = "host_info.config"
	}
}