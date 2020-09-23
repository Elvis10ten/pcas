package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.ledger.models.Ledger
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
data class Packet(
    @ProtoId(1)
    val type: Type,
    @ProtoId(2)
    val ledger: Ledger
) {

    enum class Type {
        INTRO,
        UPDATE,
        EVICT_NOTICE
    }
}