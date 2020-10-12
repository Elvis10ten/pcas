package com.fluentbuild.pcas.utils

import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal object Delegates {

	inline fun <T> observable(crossinline onChange: (newValue: T?) -> Unit): ReadWriteProperty<Any?, T?> =
		Delegates.observable(null, { _: KProperty<*>, _: T?, newValue: T? -> onChange(newValue) })
}