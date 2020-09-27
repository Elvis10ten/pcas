package com.fluentbuild.pcas.ledger.models

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.io.Address
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class EntryTest {

    private val timestampStub1 = 0L
    private val timestampStub2 = 1L

    private val entityStub1 = BondEntity(1, 2)
    private val entityStub2 = BondEntity(3, 4)

    private val hostStub1 = HostInfo(
        uuid = "uuid1",
        name = "name",
        ip = Address.Ipv4("address"),
        port = 0,
        isInteractive = false,
        sampleRateHz = 0,
        minBufferSizeBytes = 0
    )
    private val hostStub2 = HostInfo(
        uuid = "uuid2",
        name = "name",
        ip = Address.Ipv4("address"),
        port = 0,
        isInteractive = false,
        sampleRateHz = 0,
        minBufferSizeBytes = 0
    )

    @Test
    fun `equals WHEN entry's entity & host are equal SHOULD return true`() {
        val entry1 = Entry(entityStub1, hostStub1, timestampStub1)
        val entry2 = Entry(entityStub1, hostStub1, timestampStub2)
        assertEquals(entry1, entry2)
    }

    @Test
    fun `equals WHEN entry's host are unequal but entity are equal SHOULD return false`() {
        val entry1 = Entry(entityStub1, hostStub1, timestampStub1)
        val entry2 = Entry(entityStub1, hostStub2, timestampStub2)
        assertNotEquals(entry1, entry2)
    }

    @Test
    fun `equals WHEN entry's entity are unequal but host are equal SHOULD return false`() {
        val entry1 = Entry(entityStub1, hostStub1, timestampStub1)
        val entry2 = Entry(entityStub2, hostStub1, timestampStub2)
        assertNotEquals(entry1, entry2)
    }

    @Test
    fun `equals WHEN entry's entity & host are unequal SHOULD return false`() {
        val entry1 = Entry(entityStub1, hostStub1, timestampStub1)
        val entry2 = Entry(entityStub2, hostStub2, timestampStub2)
        assertNotEquals(entry1, entry2)
    }
}