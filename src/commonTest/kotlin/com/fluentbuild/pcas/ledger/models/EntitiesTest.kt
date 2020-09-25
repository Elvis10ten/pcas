package com.fluentbuild.pcas.ledger.models

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EntitiesTest {

    @Test
    fun `equals WHEN PropertyEntity & BondEntity have the same serviceId & bondId SHOULD return true`() {
        val propertyEntity1 = PropertyEntity(1, 2, true, 1.0)
        val bondEntity1 = BondEntity(1, 2)
        assertTrue(propertyEntity1.equals(bondEntity1))
    }

    @Test
    fun `equals WHEN PropertyEntity & BondEntity have different serviceId & bondId SHOULD return false`() {
        val propertyEntity1 = PropertyEntity(1, 2, true, 1.0)
        val bondEntity1 = BondEntity(3, 2)
        assertFalse(propertyEntity1.equals(bondEntity1))
    }
}