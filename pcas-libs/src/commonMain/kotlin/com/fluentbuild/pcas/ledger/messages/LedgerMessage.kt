package com.fluentbuild.pcas.ledger.messages

import com.fluentbuild.pcas.host.Uuid
import com.fluentbuild.pcas.ledger.Block
import com.fluentbuild.pcas.utils.Timestamp
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
internal sealed class LedgerMessage {

    abstract val sender: Uuid

    @Serializable
    sealed class Essential: LedgerMessage() {

        abstract val sequenceNumber: Int

        @Serializable
        data class Genesis(
            @ProtoNumber(1)
            override val sender: Uuid,
            @ProtoNumber(2)
            override val sequenceNumber: Int
        ): Essential()

        @Serializable
        data class Update(
            @ProtoNumber(1)
            override val sender: Uuid,
            @ProtoNumber(2)
            val senderBlocks: Set<Block> = emptySet(),
            @ProtoNumber(3)
            override val sequenceNumber: Int
        ): Essential()
    }

    sealed class InEssential: LedgerMessage() {

        @Serializable
        data class Heartbeat(
            @ProtoNumber(1)
            override val sender: Uuid,
            @ProtoNumber(2)
            val hostBlocksMaxTimestamp: Map<Uuid, Timestamp> = emptyMap(),
        ): InEssential()

        @Serializable
        data class Ack(
            @ProtoNumber(1)
            override val sender: Uuid,
            @ProtoNumber(2)
            val ackTarget: Uuid,
            @ProtoNumber(3)
            val ackSequenceNumber: Int
        ): InEssential()
    }
}