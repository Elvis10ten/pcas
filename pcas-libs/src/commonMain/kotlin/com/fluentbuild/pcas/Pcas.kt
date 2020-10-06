package com.fluentbuild.pcas

import com.fluentbuild.pcas.async.Cancellable
import com.fluentbuild.pcas.async.Cancellables
import com.fluentbuild.pcas.middleware.ServiceRegistry
import com.fluentbuild.pcas.services.audio.AudioStateUpdater

class Pcas internal constructor(
	private val serviceRegistry: ServiceRegistry,
	private val audioStateUpdater: AudioStateUpdater
) {

	fun run(): Cancellable {
		return Cancellables().apply {
			this += serviceRegistry.run()
			this += audioStateUpdater.start()
		}
	}
}