package com.fluentbuild.pcas.io

enum class IpTos(val value: Int) {
	LOW_COST(0x02),
	RELIABILITY(0x04),
	THROUGHPUT(0x08),
	LOW_DELAY(0x10),
}