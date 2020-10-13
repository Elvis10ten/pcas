package com.fluentbuild.pcas.utils

import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object Delegates {

	inline fun <T> observable(
		crossinline onChange: (oldValue: T?, newValue: T?) -> Unit
	): ReadWriteProperty<Any?, T?> {
		return Delegates.observable(null, { _: KProperty<*>, oldValue: T?, newValue: T? ->
			onChange(oldValue, newValue)
		})
	}

	inline fun <T> observable(
		initialValue: T,
		crossinline onChange: (newValue: T) -> Unit
	): ReadWriteProperty<Any?, T> {
		return Delegates.observable(initialValue, { _: KProperty<*>, _: T, newValue: T ->
			onChange(newValue)
		})
	}
}