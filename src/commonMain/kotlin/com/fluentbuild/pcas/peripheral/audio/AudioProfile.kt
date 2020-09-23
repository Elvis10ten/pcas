package com.fluentbuild.pcas.peripheral.audio

import com.fluentbuild.pcas.ledger.models.BondId

enum class AudioProfile(val bondId: BondId) {
    A2DP(0),
    HSP(1);

    companion object {

        fun from(bondId: BondId) = values().single { it.bondId == bondId }
    }
}