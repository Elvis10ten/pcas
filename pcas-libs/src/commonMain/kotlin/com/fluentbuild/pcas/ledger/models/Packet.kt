package com.fluentbuild.pcas.ledger.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
data class Packet(
    @ProtoNumber(1)
    val type: Type,
    @ProtoNumber(2)
    val ledger: Ledger,
    @ProtoNumber(3)
    val broadcastTimestamp: Long
) {

    enum class Type {
        INTRO,
        UPDATE,
        EVICTION_NOTICE
    }
}