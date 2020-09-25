package com.fluentbuild.pcas.ledger

import com.fluentbuild.pcas.io.Parcel
import com.fluentbuild.pcas.io.ParcelReceiver
import com.fluentbuild.pcas.ledger.models.Packet
import com.fluentbuild.pcas.utils.logger
import kotlinx.serialization.protobuf.ProtoBuf

class PacketReceiver(
    private val packetSender: PacketSender,
    private val protoBuf: ProtoBuf
): ParcelReceiver {

    private val log by logger()
    private val packetSerializer = Packet.serializer()

    override fun onReceived(parcel: Parcel) {
        /*val packet = protoBuf.load(packetSerializer, payload)
        log.info { "Packet received: $packet" }

        with(packet.ledger) {
            ledgerStore.upsert(host, getConnections(host), getCharacteristics(host))
        }

        when(packet.type) {
            Packet.Type.INTRO -> {
                packetSender.sendUpdate()
            }
            Packet.Type.UPDATE -> {}
            Packet.Type.EVICT_NOTICE -> {
                if(packet.ledger.getEvictionNotices().contains(host)) {
                    packetSender.sendUpdate()
                }
            }
        }*/
    }
}