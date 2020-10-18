package com.fluentbuild.pcas.values

interface Provider<ValueT> {

	val currentValue: ValueT
}