package com.fluentbuild.pcas.ledger.models

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.services.audio.AudioProperty
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.protobuf.ProtoNumber
import kotlin.math.log10
import kotlin.math.pow

@Serializable
data class PropertyEntity constructor(
    @ProtoNumber(1)
    override val serviceId: ServiceId,
    @ProtoNumber(2)
    override val bondId: BondId,
    @ProtoNumber(3)
    val hasUsage: Boolean,
    @ProtoNumber(4)
    val priority: Int,
    @ProtoNumber(5)
    val timestamp: Long
): Entity() {

    // todo
    fun getRank(/*owner: HostInfo, bondEntity: BondEntity*/): Double {
        val a = 5.0.pow(priority)
        //val b = booleanToInt(bondEntity.isConnected)
        val b = 1
        //val c = booleanToInt(owner.isInteractive)
        val c = 1
        val d = log10(timestamp.toDouble())
        return a * b * c * d
    }

    private fun booleanToInt(boolean: Boolean): Int {
        return if(boolean) 2 else 1
    }
}