package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.host.Uuid
import com.fluentbuild.pcas.utils.Timestamp
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
internal sealed class LedgerMessage {

    abstract val sender: Uuid

    @Serializable
    data class Genesis(
        @ProtoNumber(1)
        override val sender: Uuid
    ): LedgerMessage()

    @Serializable
    data class Heartbeat(
        @ProtoNumber(1)
        override val sender: Uuid,
        @ProtoNumber(2)
        val hostBlocksMaxTimestamp: Map<Uuid, Timestamp>,
    ): LedgerMessage()

    @Serializable
    data class Update(
        @ProtoNumber(1)
        override val sender: Uuid,
        @ProtoNumber(2)
        val senderBlocks: Set<Block>
    ): LedgerMessage()
}