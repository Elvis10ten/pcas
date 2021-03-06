package com.fluentbuild.pcas.host

import com.fluentbuild.pcas.values.Model
import com.fluentbuild.pcas.io.Address
import com.fluentbuild.pcas.io.Port
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
data class HostInfo(
	@ProtoNumber(1)
    val uuid: Uuid,
	@ProtoNumber(2)
    val name: String,
	@ProtoNumber(3)
    val address: Address.Ipv4,
	@ProtoNumber(4)
    val port: Port,
	@ProtoNumber(5)
    val isInteractive: Boolean,
	@ProtoNumber(6)
    val idealAudioBufferSizeBytes: Int
): Model<HostInfo> {

    override fun equals(other: Any?) = uuid == (other as? HostInfo)?.uuid

    override fun hashCode() = uuid.hashCode()

	override fun isAllFieldsEqual(other: HostInfo): Boolean {
		return uuid == this.uuid &&
				name == this.name &&
				address == this.address &&
				port == this.port &&
				isInteractive == this.isInteractive &&
				idealAudioBufferSizeBytes == this.idealAudioBufferSizeBytes
	}
}