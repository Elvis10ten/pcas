package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.values.Model
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.peripheral.PeripheralProfile
import com.fluentbuild.pcas.services.ServiceClass
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import kotlin.math.log10
import kotlin.math.pow

@Serializable
data class Block(
    @ProtoNumber(1)
    val serviceClass: ServiceClass,
    @ProtoNumber(2)
    val profile: PeripheralProfile,
    @ProtoNumber(3)
    val peripheral: Peripheral,
    @ProtoNumber(4)
    val priority: Int,
    @ProtoNumber(5)
    val timestamp: Long,
    @ProtoNumber(6)
    val bondSteadyState: PeripheralBond.State,
    @ProtoNumber(7)
    val owner: HostInfo,
    @ProtoNumber(8)
    val canStreamData: Boolean,
    @ProtoNumber(9)
    val canHandleDataStream: Boolean
): Model<Block> {

    val isConnected = bondSteadyState == PeripheralBond.State.CONNECTED

    val hasPriority = priority != NO_PRIORITY

    private val maxPossibleConnectionAndInteractiveScore = 4 + 2

    // Any device with a higher priority should always rank higher.
    private val priorityScore = (maxPossibleConnectionAndInteractiveScore + 1.0).pow(priority)

    private val connectionScore: Int get() {
        // Connection should contribute more if we can't stream
        val trueValue = if(canStreamData) 2 else 4
        return if(isConnected) trueValue else 1
    }

    private val interactiveScore = if(owner.isInteractive) 2 else 1


    private val timestampScore = log10(timestamp.toDouble())

    internal val rank = priorityScore + connectionScore + interactiveScore + timestampScore

    override fun equals(other: Any?): Boolean {
        if(other !is Block) return false
        return serviceClass == other.serviceClass &&
                profile == other.profile &&
                peripheral == other.peripheral &&
                owner == other.owner
    }

    fun hasContention(other: Block): Boolean {
        return serviceClass == other.serviceClass &&
                profile == other.profile &&
                peripheral == other.peripheral
    }

    override fun isAllFieldsEqual(other: Block): Boolean {
        return serviceClass == other.serviceClass &&
                profile == other.profile &&
                peripheral == other.peripheral &&
                priority == other.priority &&
                timestamp == other.timestamp &&
                bondSteadyState == other.bondSteadyState &&
                owner == other.owner &&
                canStreamData == other.canStreamData &&
                canHandleDataStream == other.canHandleDataStream
    }

    override fun hashCode(): Int {
        var result = serviceClass.hashCode()
        result = 31 * result + profile.hashCode()
        result = 31 * result + peripheral.hashCode()
        result = 31 * result + owner.hashCode()
        return result
    }

    companion object {

        const val NO_PRIORITY = 0
    }
}