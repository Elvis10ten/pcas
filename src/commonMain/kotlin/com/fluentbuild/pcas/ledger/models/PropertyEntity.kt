package com.fluentbuild.pcas.ledger.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId
import kotlin.math.hypot
import kotlin.math.log10
import kotlin.math.pow

@Serializable
// peripheralUuid
data class PropertyEntity(
    @ProtoId(1)
    override val serviceId: ServiceId,
    @ProtoId(2)
    override val bondId: BondId,
    @ProtoId(3)
    @Deprecated("Use rank")
    override val timestamp: Long,
    @ProtoId(4)
    val hasUsage: Boolean,
    @Deprecated("Use rank")
    @ProtoId(5)
    val priority: Int = NO_PRIORITY,
): Entity {

    // todo
    val isConnected = false
    val isInteractive = false
    val rank = (5.0.pow(priority)) * booleanToInt(isConnected) * booleanToInt(isInteractive) * log10(timestamp.toDouble())

    private fun booleanToInt(boolean: Boolean): Int {
        return if(boolean) 2 else 1
    }

    override fun equals(other: Any?) = isEqual(other)

    override fun hashCode() = getHashcode()

    companion object {
        const val NO_PRIORITY = 0
    }
}