package com.fluentbuild.pcas.peripheral.audio

import com.fluentbuild.pcas.ledger.models.BondId

enum class AudioProfile(val bondId: BondId) {
    A2DP(0),
    HSP(1);

    companion object {

        private val values = values()

        fun from(bondId: BondId) = values.single { it.bondId == bondId }
    }
}