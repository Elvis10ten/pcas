package com.fluentbuild.pcas.ledger.models

import com.fluentbuild.pcas.host.HostInfo
import com.fluentbuild.pcas.io.Address
import com.fluentbuild.pcas.ledger.utils.assertContentEquals
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LedgerTest {

    private val currentTimestampStub = 1601059468952L

    private val ownerStub = HostInfo(
        uuid = "owner1",
        name = "name",
        address = Address.Ipv4("address"),
        port = 0,
        isInteractive = false,
        minBufferSizeBytes = 0
    )
    private val otherStub1 = HostInfo(
        uuid = "other1",
        name = "name",
        address = Address.Ipv4("address"),
        port = 0,
        isInteractive = false,
        minBufferSizeBytes = 0
    )
    private val otherStub2 = HostInfo(
        uuid = "other2",
        name = "name",
        address = Address.Ipv4("address"),
        port = 0,
        isInteractive = false,
        minBufferSizeBytes = 0
    )

    private val ownerFreshBondStub = Entry(
        entity = BondEntity(1, 2),
        host = ownerStub,
        timestamp = currentTimestampStub
    )
    private val other1FreshBondStub = Entry(
        entity = BondEntity(1, 2),
        host = otherStub1,
        timestamp = currentTimestampStub
    )

    private val ownerFreshPropStub = Entry(
        entity = PropertyEntity(1, 2, true, 1.0),
        host = ownerStub,
        timestamp = currentTimestampStub
    )
    private val other1FreshPropStub = Entry(
        entity = PropertyEntity(1, 2, true, 1.0),
        host = otherStub1,
        timestamp = currentTimestampStub
    )
    private val other2FreshPropStubNotInLedgerStub = Entry(
        entity = PropertyEntity(1, 2, true, 1.0),
        host = otherStub2,
        timestamp = currentTimestampStub
    )

    private val other1StaleBondStub = Entry(
        entity = BondEntity(1, 2),
        host = otherStub1,
        timestamp = currentTimestampStub - Ledger.ENTRY_EVICTION_NOTICE_THRESHOLD_MILLIS - 1
    )

    private val ledger = Ledger(
        ownerStub,
        setOf(ownerFreshBondStub, other1StaleBondStub, other1FreshBondStub),
        setOf(ownerFreshPropStub, other1FreshPropStub)
    )

    @Test
    fun `ownerBonds SHOULD return only owner bonds`() {
        assertContentEquals(setOf(ownerFreshBondStub), ledger.ownerBonds)
    }

    @Test
    fun `othersBonds SHOULD return only others bonds`() {
        assertContentEquals(setOf(other1StaleBondStub, other1FreshBondStub), ledger.othersBonds)
    }

    @Test
    fun `ownerProps SHOULD return only owner props`() {
        assertContentEquals(setOf(ownerFreshPropStub), ledger.ownerProps)
    }

    @Test
    fun `othersProps SHOULD return only others props`() {
        assertContentEquals(setOf(other1FreshPropStub), ledger.othersBonds)
    }

    @Test
    fun `hasBond WHEN matching boundEntity exists SHOULD return true`() {
        assertTrue(ledger.hasBond(ownerFreshPropStub))
        assertTrue(ledger.hasBond(other1FreshPropStub))
    }

    @Test
    fun `hasConnection WHEN no matching boundEntity exists SHOULD return false`() {
        assertFalse(ledger.hasBond(other2FreshPropStubNotInLedgerStub))
    }

    @Test
    fun `getEvictionNotices SHOULD return hosts with eviction notices`() {
        assertContentEquals(setOf(other1StaleBondStub.host), ledger.getEvictionNotices(currentTimestampStub))
    }

    // TODO: Verify that getEvictionNotices never returns owner
}