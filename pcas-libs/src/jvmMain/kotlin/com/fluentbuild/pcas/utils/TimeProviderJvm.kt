package com.fluentbuild.pcas.utils

internal open class TimeProviderJvm: TimeProvider {

	override val currentTimeMillis get() = System.currentTimeMillis()

	override val elapsedRealtime: ElapsedRealtime get() = TODO("Not yet implemented")
}