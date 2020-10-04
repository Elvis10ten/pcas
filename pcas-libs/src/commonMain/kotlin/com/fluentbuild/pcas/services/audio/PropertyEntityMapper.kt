package com.fluentbuild.pcas.services.audio

import com.fluentbuild.pcas.ledger.models.PropertyEntity
import com.fluentbuild.pcas.services.ServiceId
import com.fluentbuild.pcas.peripheral.audio.AudioProfile
import com.fluentbuild.pcas.services.Mapper
import com.fluentbuild.pcas.utils.TimeProvider

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
        priority = apexUsage?.priority ?: AudioProperty.NO_PRIORITY,
        timestamp = timeProvider.currentTimeMillis()
    )
}