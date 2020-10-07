package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.host.HostInfoObservable
import com.fluentbuild.pcas.ledger.Block
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.services.audio.AudioProperty.Usage
import com.fluentbuild.pcas.peripheral.audio.AudioProfile
import com.fluentbuild.pcas.services.AUDIO_SERVICE_ID
import com.fluentbuild.pcas.utils.TimeProvider

internal class AudioBlocksBuilder(
    private val audioPeripheral: Peripheral,
	private val timeProvider: TimeProvider,
    private val hostObservable: HostInfoObservable
) {

    private var a2dpBondCache: PeripheralBond? = null
    private var hspBondCache: PeripheralBond? = null

    private var a2dpUsagesCache: Set<Usage>? = null
    private var hspUsagesCache: Set<Usage>? = null

    private var hasA2dpChanged = false
    private var hasHspChanged = false

    fun setBond(bond: PeripheralBond) {
        when (bond.bondId) {
            AudioProfile.A2DP.bondId -> {
                hasA2dpChanged = true
                a2dpBondCache = bond
            }
            AudioProfile.HSP.bondId -> {
                hasHspChanged = true
                hspBondCache = bond
            }
            else -> error("$bond is not supported in audio service")
        }
    }

    fun setProperty(property: AudioProperty) {
        if(a2dpUsagesCache != property.unidirectionalUsages) {
            hasA2dpChanged = true
            a2dpUsagesCache = property.unidirectionalUsages
        }

        if(hspUsagesCache != property.bidirectionalUsages) {
            hasHspChanged = true
            hspUsagesCache = property.bidirectionalUsages
        }
    }

    fun buildNovel(): Set<Block>? {
        val a2dpBond = a2dpBondCache ?: return null
        val hspBond = hspBondCache ?: return null
        val a2dpUsages = a2dpUsagesCache ?: return null
        val hspUsages = hspUsagesCache ?: return null

        if(!hasA2dpChanged && !hasHspChanged) return null

        return LinkedHashSet<Block>(NUM_BONDS).apply {
            if(hasA2dpChanged) {
                this += createBlock(a2dpUsages, a2dpBond)
                hasA2dpChanged = false
            }

            if(hasHspChanged) {
                this += createBlock(hspUsages, hspBond)
                hasHspChanged = false
            }
        }
    }

    private fun createBlock(usages: Set<Usage>, bond: PeripheralBond) = Block(
        serviceId = AUDIO_SERVICE_ID,
        bondId = bond.bondId,
        peripheral = audioPeripheral,
        priority = usages.maxOfOrNull { it.priority } ?: Block.NO_PRIORITY,
        timestamp = timeProvider.currentTimeMillis(),
        bondState = bond.state,
        owner = hostObservable.currentValue
    )

    companion object {

        private const val NUM_BONDS = 2
    }
}