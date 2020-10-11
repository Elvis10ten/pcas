package com.fluentbuild.pcas.ledger

/*import com.fluentbuild.pcas.HostInfo
import com.fluentbuild.pcas.ledger.models.PropertyEntity
import com.fluentbuild.pcas.ledger.models.BondEntity
import com.fluentbuild.pcas.ledger.Ledger
import com.fluentbuild.pcas.utils.TimeProvider
import io.mockk.called
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LedgerStoreTest {

    /*private lateinit var ledgerStore: LedgerStore
    private lateinit var onChangedCallback: Function1<Ledger, Unit>
    private val host1 = HostInfo("u1", "n1", 1, 1)
    private val host2 = HostInfo("u2", "n2", 2, 2)

    @BeforeTest
    fun setup() {
        onChangedCallback = mockk(relaxed = true)
        ledgerStore = LedgerStore(onChangedCallback, TimeProvider())
    }

    @Test
    fun `WHEN instantiated THEN onChanged callback is not called`() {
        verify { onChangedCallback(any()) wasNot called }
    }

    @Test
    fun `upsert WHEN called with no existing entries THEN adds to ledger`() {
        val host1Connections = setOf(createConnection(1), createConnection(2))
        val host1Characteristics = setOf(createCharacteristics(1), createCharacteristics(2))

        ledgerStore.upsert(host1, host1Connections, host1Characteristics)

        val ledger = ledgerStore.get()
        assertEquals(host1Connections.toList(), ledger.bondEntries.map { it.entity })
        assertEquals(host1Characteristics.toList(), ledger.propEntries.map { it.entity })
    }

    @Test
    fun `upsert WHEN called with existing entries THEN updates entries in ledger`() {
        val host1Connections = setOf(createConnection(1), createConnection(2))
        val host1Characteristics = setOf(createCharacteristics(1), createCharacteristics(2))

        ledgerStore.upsert(host1, host1Connections, host1Characteristics)
        ledgerStore.upsert(host1, host1Connections, host1Characteristics)

        val ledger = ledgerStore.get()
        assertEquals(host1Connections.toList(), ledger.bondEntries.map { it.entity })
        assertEquals(host1Characteristics.toList(), ledger.propEntries.map { it.entity })
    }

    @Test
    fun `evict WHEN called THEN removes all entries for specified host`() {
        val host1Connections = setOf(createConnection(1), createConnection(2))
        val host1Characteristics = setOf(createCharacteristics(1), createCharacteristics(2))
        val host2Connections = setOf(createConnection(3), createConnection(4))
        val host2Characteristics = setOf(createCharacteristics(3), createCharacteristics(4))

        ledgerStore.upsert(host1, host1Connections, host1Characteristics)
        ledgerStore.upsert(host2, host2Connections, host2Characteristics)
        ledgerStore.evict(host1)

        val ledger = ledgerStore.get()
        assertEquals(host2Connections.toList(), ledger.bondEntries.map { it.entity })
        assertEquals(host2Characteristics.toList(), ledger.propEntries.map { it.entity })
    }

    private fun createConnection(num: Int) =
        BondEntity(num, num)

    private fun createCharacteristics(num: Int) =
        PropertyEntity(num, num, num, false)*/
}*/