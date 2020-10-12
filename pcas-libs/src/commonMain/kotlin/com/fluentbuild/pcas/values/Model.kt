package com.fluentbuild.pcas.values

interface Model<ModelT> {

	fun isAllFieldsEqual(other: ModelT): Boolean
}