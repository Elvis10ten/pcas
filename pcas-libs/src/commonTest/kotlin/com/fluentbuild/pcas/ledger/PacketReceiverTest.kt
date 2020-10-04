package com.fluentbuild.pcas.ledger

/*import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.ledger.Ledger
import io.mockk.mockk
import io.mockk.verify
import kotlinx.serialization.protobuf.ProtoBuf
import kotlin.test.BeforeTest
import kotlin.test.Test

class PacketReceiverTest {

    private val protoBuf = ProtoBuf()
    private val packetSerializer = Packet.serializer()
    private val host1 = HostInfo("u1", "n1", 1, 1)

    private lateinit var ledgerStore: LedgerStore
    private lateinit var packetSender: PacketSender
    private lateinit var packetReceiver: PacketReceiver

    @BeforeTest
    fun setup() {
        ledgerStore = mockk()
        packetSender = mockk()
        packetReceiver = PacketReceiver(ledgerStore, packetSender, protoBuf)
    }

    @Test
    fun `onReceived`() {
        val packet = Packet(Packet.Type.INTRO, Ledger())
        packetReceiver.onReceived(host1, protoBuf.dump(packetSerializer, packet))
        verify { ledgerStore.upsert(eq(host1), any(), any()) }
    }
}*/