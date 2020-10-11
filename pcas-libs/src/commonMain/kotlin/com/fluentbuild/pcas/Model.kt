package com.fluentbuild.pcas

interface Model<ModelT> {

	fun isDuplicate(other: ModelT): Boolean
}