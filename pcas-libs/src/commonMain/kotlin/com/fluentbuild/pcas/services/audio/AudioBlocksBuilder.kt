package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.host.HostInfoObservable
import com.fluentbuild.pcas.ledger.Block
import com.fluentbuild.pcas.peripheral.Peripheral
import com.fluentbuild.pcas.peripheral.PeripheralBond
import com.fluentbuild.pcas.peripheral.PeripheralProfile
import com.fluentbuild.pcas.services.ServiceClass
import com.fluentbuild.pcas.services.audio.AudioProperty.Usage
import com.fluentbuild.pcas.utils.*
import com.fluentbuild.pcas.utils.Delegates.observable
import com.fluentbuild.pcas.utils.TimeProvider
import com.fluentbuild.pcas.utils.mapSetNotNullMutable

internal class AudioBlocksBuilder(
	private val audioPeripheral: Peripheral,
    private val canCaptureAudio: Boolean,
	private val timeProvider: TimeProvider,
	private val hostObservable: HostInfoObservable
) {

    private val profileCaches = mapOf(
        PeripheralProfile.A2DP to Cache(),
        PeripheralProfile.HEADSET to Cache()
    )

    fun setBond(bond: PeripheralBond) {
        val cache = profileCaches.getValue(bond.profile)
        if(cache.bond != bond) {
            cache.bond = bond
        }
    }

    fun setProperty(property: AudioProperty) {
        profileCaches.forEach { (profile, cache) ->
            val profileUsages = property.usages.filterSet { profile == it.profile }
            if(cache.usages != profileUsages) {
                cache.usages = profileUsages
            }
        }
    }

    fun buildNovel(consumer: (Set<Block>) -> Unit) {
        val blocks = profileCaches.values.mapSetNotNullMutable { it.build() }
        if(blocks.isNotEmpty()) {
            consumer(blocks)
        }
    }

    private inner class Cache {

        var bond: PeripheralBond? by observable { _, _ -> isDirty = true }
        var usages: Set<Usage>? by observable { _, _ -> isDirty = true }
        private var isDirty: Boolean = false

        fun build(): Block? {
            val blockBond = bond ?: return null
            val blockUsages = usages ?: return null
            if(!isDirty) return null

            isDirty = false
            return Block(
                serviceClass = ServiceClass.AUDIO,
                profile = blockBond.profile,
                peripheral = audioPeripheral,
                priority = blockUsages.maxOfOrNull { it.priority } ?: Block.NO_PRIORITY,
                timestamp = timeProvider.currentTimeMillis,
                bondState = blockBond.state,
                owner = hostObservable.currentValue,
                canStreamData = canCaptureAudio && blockBond.profile.supportsStreaming,
                canHandleDataStream = blockBond.profile.supportsStreaming
            )
        }
    }
}