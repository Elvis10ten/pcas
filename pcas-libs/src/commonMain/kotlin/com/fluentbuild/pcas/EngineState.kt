package com.fluentbuild.pcas

import com.fluentbuild.pcas.host.HostConfig
import com.fluentbuild.pcas.ledger.Ledger

data class EngineState(
	val engineStatus: Engine.Status = Engine.Status.IDLE,
	val ledger: Ledger? = null
) {

	lateinit var hostConfig: HostConfig
}