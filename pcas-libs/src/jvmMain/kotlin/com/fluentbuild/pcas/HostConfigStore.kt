package com.fluentbuild.pcas

import com.fluentbuild.pcas.host.HostConfig
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.utils.AtomicFile
import com.fluentbuild.pcas.utils.createRandomUuid
import com.fluentbuild.pcas.utils.decode
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.File

class HostConfigStore(
	private val protoBuf: ProtoBuf,
	privateFilesDir: File,
	private val nameProvider: () -> String
) {

	private val atomicFile = AtomicFile(File(privateFilesDir, HOST_CONFIG_FILE_NAME))

	fun upsert(audioPeripheral: Peripheral, networkKey: ByteArray) {
		val hostConfig = get()
		val uuid = hostConfig?.uuid ?: createRandomUuid()
		val name = hostConfig?.name ?: nameProvider()
		val newConfig = HostConfig(uuid, name, networkKey, audioPeripheral)

		atomicFile.startWrite().apply {
			write(protoBuf.encodeToByteArray(newConfig))
			atomicFile.finishWrite(this)
		}
	}

	fun get(): HostConfig? {
		return try {
			protoBuf.decode<HostConfig>(atomicFile.openRead().readAllBytes())
		} catch (e: Exception) {
			null
		}
	}

	companion object {

		private const val HOST_CONFIG_FILE_NAME = "host_info.config"
	}
}