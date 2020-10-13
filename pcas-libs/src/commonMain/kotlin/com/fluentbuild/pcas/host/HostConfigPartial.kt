package com.fluentbuild.pcas.host

import com.fluentbuild.pcas.peripheral.Peripheral
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
data class HostConfigPartial(
	@ProtoNumber(1)
	val uuid: Uuid,
	@ProtoNumber(2)
	val name: String,
	@ProtoNumber(3)
	val networkKey: ByteArray,
	@ProtoNumber(4)
	val audioPeripheral: Peripheral? = null,
) {

	@Transient
	var canCaptureAudio: Boolean = false

	fun createFull(): HostConfig {
		return HostConfig(
			uuid,
			name,
			networkKey,
			audioPeripheral!!,
			canCaptureAudio
		)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || this::class != other::class) return false

		other as HostConfigPartial

		if (uuid != other.uuid) return false
		if (name != other.name) return false
		if (!networkKey.contentEquals(other.networkKey)) return false
		if (audioPeripheral != other.audioPeripheral) return false
		if (canCaptureAudio != other.canCaptureAudio) return false

		return true
	}

	override fun hashCode(): Int {
		var result = uuid.hashCode()
		result = 31 * result + name.hashCode()
		result = 31 * result + networkKey.contentHashCode()
		result = 31 * result + (audioPeripheral?.hashCode() ?: 0)
		result = 31 * result + canCaptureAudio.hashCode()
		return result
	}
}