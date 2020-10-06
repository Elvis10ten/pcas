package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.peripheral.BondId
import com.fluentbuild.pcas.services.ServiceId
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.utils.unsafeLazy
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import kotlin.math.log10
import kotlin.math.pow

@Serializable
data class Block(
    @ProtoNumber(1)
    val serviceId: ServiceId,
    @ProtoNumber(2)
    val bondId: BondId,
    @ProtoNumber(3)
    val priority: Int,
    @ProtoNumber(4)
    val timestamp: Long,
    @ProtoNumber(5)
    val bondState: PeripheralBond.State,
    @ProtoNumber(6)
    val host: HostInfo
) {

    val isConnected = bondState == PeripheralBond.State.CONNECTED

    val hasPriority = priority != NO_PRIORITY

    val rank by unsafeLazy {
        val a = 5.0.pow(priority)
        val b = booleanToInt(isConnected)
        val c = booleanToInt(host.isInteractive)
        val d = log10(timestamp.toDouble())
        a * b * c * d
    }

    private fun booleanToInt(boolean: Boolean): Int {
        return if(boolean) 2 else 1
    }

    override fun equals(other: Any?): Boolean {
        if(other !is Block) return false
        return serviceId == other.serviceId && bondId == other.bondId && host == other.host
    }

    override fun hashCode(): Int {
        var result = serviceId
        result = 31 * result + bondId
        result = 31 * result + host.hashCode()
        return result
    }

    companion object {

        const val NO_PRIORITY = 0
    }
}