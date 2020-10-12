package com.fluentbuild.pcas.utils

import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object Delegates {

	internal inline fun <ObjectT: Any?> observable(crossinline onChange: (newValue: ObjectT?) -> Unit): ReadWriteProperty<Any?, ObjectT?> =
		Delegates.observable(null, { _: KProperty<*>, _: ObjectT?, newValue: ObjectT? -> onChange(newValue) })
}