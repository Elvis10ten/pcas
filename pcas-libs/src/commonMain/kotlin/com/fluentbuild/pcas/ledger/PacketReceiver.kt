package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.io.MessageReceiver
import com.fluentbuild.pcas.ledger.models.Packet
import com.fluentbuild.pcas.utils.logger
import kotlinx.serialization.protobuf.ProtoBuf

class PacketReceiver(
    private val protoBuf: ProtoBuf,
    private val packetBroadcaster: PacketBroadcaster,
    private val ledgerStore: LedgerStore
): MessageReceiver {

    private val log by logger()
    private val packetSerializer = Packet.serializer()

    override fun onReceived(data: ByteArray) {
        val packet = protoBuf.decodeFromByteArray(packetSerializer, data)
        log.info { "Packet received: $packet" }

        with(packet.ledger) {
            ledgerStore.upsert(owner, ownerBonds.mapToEntities(), ownerProps.mapToEntities())
        }

        when(packet.type) {
            Packet.Type.INTRO -> {
                packetBroadcaster.broadcastUpdate()
            }
            Packet.Type.UPDATE -> {
                // Do nothing: Ledger update already upserted.
            }
            Packet.Type.EVICTION_NOTICE -> {
                val evictionNotices = packet.ledger.getEvictionNotices(packet.broadcastTimestamp)
                val self = ledgerStore.get().owner
                if(evictionNotices.contains(self)) {
                    packetBroadcaster.broadcastUpdate()
                }
            }
        }
    }
}