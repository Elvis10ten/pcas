package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.io.MulticastChannel
import com.fluentbuild.pcas.ledger.models.Packet
import com.fluentbuild.pcas.utils.TimeProvider
import com.fluentbuild.pcas.utils.logger
import kotlinx.serialization.protobuf.ProtoBuf

class PacketBroadcaster(
    private val protoBuf: ProtoBuf,
    private val multicastChannel: MulticastChannel,
    private val ledgerStore: LedgerStore,
    private val timeProvider: TimeProvider
) {

    private val log by logger()
    private val packetSerializer = Packet.serializer()

    fun broadcastIntro() = broadcast(createPacket(Packet.Type.INTRO))

    fun broadcastUpdate() = broadcast(createPacket(Packet.Type.UPDATE))

    fun broadcastEvictionNotice() = broadcast(createPacket(Packet.Type.EVICTION_NOTICE))

    private fun broadcast(packet: Packet) {
        log.debug { "Broadcasting packet: $packet" }
        multicastChannel.broadcast(protoBuf.dump(packetSerializer, packet))
    }

    private fun createPacket(type: Packet.Type) = Packet(type, ledgerStore.get(), timeProvider.currentTimeMillis())
}