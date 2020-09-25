package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.io.MulticastChannel
import com.fluentbuild.pcas.ledger.models.Ledger
import com.fluentbuild.pcas.ledger.models.Packet
import com.fluentbuild.pcas.utils.logger
import kotlinx.serialization.protobuf.ProtoBuf

class PacketSender(
    private val protoBuf: ProtoBuf,
    private val multicastChannel: MulticastChannel,
) {

    private val log by logger()
    private val packetSerializer = Packet.serializer()

    fun sendIntro(ledger: Ledger) {
        broadcast(createPacket(Packet.Type.INTRO))
    }

    fun sendUpdate(ledger: Ledger) {
        broadcast(createPacket(Packet.Type.UPDATE))
    }

    fun sendEvictionNotices(ledger: Ledger) {
        broadcast(createPacket(Packet.Type.EVICT_NOTICE))
    }

    private fun broadcast(packet: Packet) {
        log.debug { "Broadcasting packet: $packet" }
        //multicastChannel.broadcast(protoBuf.dump(packetSerializer, packet))
    }

    private fun createPacket(type: Packet.Type): Packet {
        TODO()
        //return Packet(type, ledgerStore.get())
    }
}