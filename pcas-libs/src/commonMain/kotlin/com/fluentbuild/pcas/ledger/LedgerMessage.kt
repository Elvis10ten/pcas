package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.host.HostUuid
import com.fluentbuild.pcas.utils.Timestamp
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
sealed class LedgerMessage {

    abstract val senderUuid: HostUuid

    @Serializable
    data class Genesis(
        @ProtoNumber(1)
        override val senderUuid: HostUuid
    ): LedgerMessage()

    @Serializable
    data class Exodus(
        @ProtoNumber(1)
        override val senderUuid: HostUuid
    ): LedgerMessage()

    @Serializable
    data class Heartbeat(
        @ProtoNumber(1)
        override val senderUuid: HostUuid,
        @ProtoNumber(2)
        val hostBlocksMaxTimestamps: Map<HostUuid, Timestamp>,
    ): LedgerMessage()

    @Serializable
    data class Update(
        @ProtoNumber(1)
        override val senderUuid: HostUuid,
        @ProtoNumber(2)
        val senderBlocks: Set<Block>
    ): LedgerMessage()
}