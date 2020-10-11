package com.fluentbuild.pcas.utils

internal open class TimeProviderJvm: TimeProvider {

	override fun currentTimeMillis() = System.currentTimeMillis()

	override fun getElapsedRealtime(): Long {
		TODO("Not yet implemented")
	}
}