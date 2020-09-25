package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.host.audio.AudioProperty
import com.fluentbuild.pcas.ledger.models.PropertyEntity
import com.fluentbuild.pcas.ledger.models.ServiceId
import com.fluentbuild.pcas.peripheral.audio.AudioProfile
import com.fluentbuild.pcas.utils.Mapper
import com.fluentbuild.pcas.utils.TimeProvider
import kotlin.math.log10
import kotlin.math.pow

class PropertyEntityMapper(
    private val audioServiceId: ServiceId,
    private val timeProvider: TimeProvider
): Mapper<AudioProperty, Set<PropertyEntity>> {

    override fun map(from: AudioProperty) = setOf(
        createA2dpPropEntity(from),
        createHspPropEntity(from)
    )

    private fun createA2dpPropEntity(audioProperty: AudioProperty) = createPropEntity(
        AudioProfile.A2DP,
        audioProperty.unidirectionalUsages.maxByOrNull { it.priority }
    )

    private fun createHspPropEntity(audioProperty: AudioProperty) = createPropEntity(
        AudioProfile.HSP,
        audioProperty.bidirectionalUsages.maxByOrNull { it.priority }
    )

    private fun createPropEntity(profile: AudioProfile, apexUsage: AudioProperty.Usage?) = PropertyEntity(
        serviceId = audioServiceId,
        bondId = profile.bondId,
        hasUsage = apexUsage != null,
        rank = 1.0 // todo
    )

    /*val rank = (5.0.pow(priority)) * booleanToInt(isConnected) * booleanToInt(isInteractive) * log10(timestamp.toDouble())

    private fun booleanToInt(boolean: Boolean): Int {
        return if(boolean) 2 else 1
    }*/
}