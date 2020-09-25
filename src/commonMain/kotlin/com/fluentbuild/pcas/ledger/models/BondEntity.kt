package com.fluentbuild.pcas.ledger.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
data class BondEntity(
    @ProtoId(1)
    override val serviceId: ServiceId,
    @ProtoId(2)
    override val bondId: BondId
): Entity()