package com.fluentbuild.pcas.ledger

/*import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.io.Address
import com.fluentbuild.pcas.ledger.models.BondEntity
import com.fluentbuild.pcas.ledger.models.Entry
import com.fluentbuild.pcas.ledger.utils.assertContentEquals
import kotlin.test.Test

class LedgerExtsTest {

    private val timestampStub = 0L

    private val hostStub1 = HostInfo(
        uuid = "uuid1",
        name = "name",
        address = Address.Ipv4("address"),
        port = 0,
        isInteractive = false,
        minBufferSizeBytes = 0
    )
    private val hostStub2 = HostInfo(
        uuid = "uuid2",
        name = "name",
        address = Address.Ipv4("address"),
        port = 0,
        isInteractive = false,
        minBufferSizeBytes = 0
    )
    private val hostStub3 = HostInfo(
        uuid = "uuid3",
        name = "name",
        address = Address.Ipv4("address"),
        port = 0,
        isInteractive = false,
        minBufferSizeBytes = 0
    )
    private val hostStub4 = HostInfo(
        uuid = "uuid4",
        name = "name",
        address = Address.Ipv4("address"),
        port = 0,
        isInteractive = false,
        minBufferSizeBytes = 0
    )

    private val host1BondStub = Entry(
        entity = BondEntity(1, 2),
        host = hostStub1,
        timestamp = timestampStub
    )
    private val host2BondStub = Entry(
        entity = BondEntity(1, 2),
        host = hostStub2,
        timestamp = timestampStub
    )
    private val host3BondStub = Entry(
        entity = BondEntity(1, 2),
        host = hostStub3,
        timestamp = timestampStub
    )

    private val bonds = setOf(host1BondStub, host2BondStub, host3BondStub)

    @Test
    fun `filterNotHost WHEN entries contains specified host SHOULD return entries without specified host`() {
        assertContentEquals(setOf(host1BondStub, host3BondStub), bonds.filterNotHost(hostStub2))
    }

    @Test
    fun `filterNotHost WHEN entries doesn't contain specified host SHOULD return entries unmodified`() {
        assertContentEquals(bonds, bonds.filterNotHost(hostStub4))
    }

    @Test
    fun `filterHost WHEN entries contains specified host SHOULD return entries with only specified host`() {
        assertContentEquals(setOf(host3BondStub), bonds.filterHost(hostStub3))
    }

    @Test
    fun `filterHost WHEN entries doesn't contain specified host SHOULD return empty entries`() {
        assertContentEquals(emptySet(), bonds.filterHost(hostStub4))
    }
}*/