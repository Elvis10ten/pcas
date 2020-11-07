package com.fluentbuild.pcas.io

interface AtomicFile {

	val isExist: Boolean

	fun read(): ByteArray

	fun write(data: ByteArray)
}