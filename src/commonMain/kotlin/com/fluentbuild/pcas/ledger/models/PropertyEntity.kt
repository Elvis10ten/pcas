package com.fluentbuild.pcas.ledger.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
data class PropertyEntity constructor(
    @ProtoId(1)
    override val serviceId: ServiceId,
    @ProtoId(2)
    override val bondId: BondId,
    @ProtoId(3)
    val hasUsage: Boolean,
    @ProtoId(4)
    val rank: Double
): Entity()