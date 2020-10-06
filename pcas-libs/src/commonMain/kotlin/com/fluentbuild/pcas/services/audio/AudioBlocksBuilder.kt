package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.host.HostInfoObservable
import com.fluentbuild.pcas.ledger.Block
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.services.audio.AudioProperty.Usage
import com.fluentbuild.pcas.peripheral.audio.AudioProfile
import com.fluentbuild.pcas.services.AUDIO_SERVICE_ID
import com.fluentbuild.pcas.utils.TimeProvider

// todo: refactor
internal class AudioBlocksBuilder(
	private val timeProvider: TimeProvider,
    private val hostObservable: HostInfoObservable
) {

    private var bondsCache: Set<PeripheralBond>? = null
    private var propCache: AudioProperty? = null

    private var lastA2dpBlock: Block? = null
    private var lastHspBlock: Block? = null

    fun setProp(newProp: AudioProperty) {
        when {
            newProp.bidirectionalUsages != propCache?.bidirectionalUsages -> {
                lastHspBlock = null
            }
            newProp.unidirectionalUsages != propCache?.unidirectionalUsages -> {
                lastA2dpBlock = null
            }
        }
        propCache = newProp
    }

    fun setBonds(newBonds: Set<PeripheralBond>) {
        when {
            newBonds.getBond(AudioProfile.HSP) != bondsCache?.getBond(AudioProfile.HSP) -> {
                lastHspBlock = null
            }
            newBonds.getBond(AudioProfile.A2DP) != bondsCache?.getBond(AudioProfile.A2DP) -> {
                lastA2dpBlock = null
            }
        }
        bondsCache = newBonds
    }

    fun buildNew(): Set<Block>? {
        val bonds = bondsCache ?: return null
        val prop = propCache ?: return null

        if(lastA2dpBlock != null && lastHspBlock != null) {
            return null
        }

        val a2dpBlock = lastA2dpBlock ?: createBlock(AudioProfile.A2DP, prop.unidirectionalUsages, bonds)
        val hspBlock = lastHspBlock ?: createBlock(AudioProfile.HSP, prop.bidirectionalUsages, bonds)
        lastA2dpBlock = a2dpBlock
        lastHspBlock = hspBlock

        return setOf(a2dpBlock, hspBlock)
    }

    fun clear() {
        bondsCache = null
        propCache = null
        lastA2dpBlock = null
        lastHspBlock = null
    }

    private fun createBlock(profile: AudioProfile, usages: Set<Usage>, bonds: Set<PeripheralBond>): Block {
        return Block(
            serviceId = AUDIO_SERVICE_ID,
            bondId = profile.bondId,
            priority = usages.maxOfOrNull { it.priority } ?: Block.NO_PRIORITY,
            timestamp = timeProvider.currentTimeMillis(),
            bondState = bonds.single { it.bondId == profile.bondId }.state,
            owner = hostObservable.currentValue
        )
    }

    private fun Set<PeripheralBond>.getBond(profile: AudioProfile) = single { it.bondId == profile.bondId }
}