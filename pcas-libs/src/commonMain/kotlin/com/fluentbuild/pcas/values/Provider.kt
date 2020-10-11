package com.fluentbuild.pcas.values

internal fun interface Provider<ValueT> {

	fun get(): ValueT
}