package com.fluentbuild.pcas

import com.fluentbuild.pcas.ledger.Ledger

data class AppState(
	val engineStatus: Engine.Status = Engine.Status.IDLE,
	val ledger: Ledger? = null,
	val richLogLines: List<String> = emptyList()
)