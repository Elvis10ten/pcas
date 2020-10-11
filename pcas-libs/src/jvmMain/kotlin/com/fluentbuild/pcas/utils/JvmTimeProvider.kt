package com.fluentbuild.pcas.utils

internal open class JvmTimeProvider: TimeProvider {

	override fun currentTimeMillis() = System.currentTimeMillis()

	override fun getElapsedRealtime(): Long {
		TODO("Not yet implemented")
	}
}