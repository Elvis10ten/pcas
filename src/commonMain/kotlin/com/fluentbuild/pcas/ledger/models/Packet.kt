package com.fluentbuild.pcas.ledger.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
data class Packet(
    @ProtoId(1)
    val type: Type,
    @ProtoId(2)
    val ledger: Ledger,
    @ProtoId(3)
    val broadcastTimestamp: Long
) {

    enum class Type {
        INTRO,
        UPDATE,
        EVICTION_NOTICE
    }
}