package com.fluentbuild.pcas.values

internal interface Provider<ValueT> {

	fun get(): ValueT
}