package com.fluentbuild.pcas.peripheral

data class StaticConfig(
	val id: String,
	val isConcurrencySupported: Boolean
) {

	companion object {

		private val CONFIGS = listOf(
			StaticConfig(id = "WH-100XM3", isConcurrencySupported = true),
			StaticConfig(id = "PXC 550", isConcurrencySupported = true),
			StaticConfig(id = "MPOW-059", isConcurrencySupported = false)
		)

		private operator fun get(id: String) = CONFIGS.find { it.id == id }

		fun isConcurrencySupported(id: String) = StaticConfig[id]?.isConcurrencySupported ?: false
	}
}