package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.host.Uuid
import com.fluentbuild.pcas.utils.Timestamp
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
sealed class LedgerMessage {

    abstract val sender: Uuid

    @Serializable
    data class Genesis(
        @ProtoNumber(1)
        override val sender: Uuid
    ): LedgerMessage()

    @Serializable
    data class Exodus(
        @ProtoNumber(1)
        override val sender: Uuid
    ): LedgerMessage()

    @Serializable
    data class Heartbeat(
        @ProtoNumber(1)
        override val sender: Uuid,
        @ProtoNumber(2)
        val hostBlocksMaxTimestamps: Map<Uuid, Timestamp>,
    ): LedgerMessage()

    @Serializable
    data class Update(
        @ProtoNumber(1)
        override val sender: Uuid,
        @ProtoNumber(2)
        val senderBlocks: Set<Block>
    ): LedgerMessage()
}