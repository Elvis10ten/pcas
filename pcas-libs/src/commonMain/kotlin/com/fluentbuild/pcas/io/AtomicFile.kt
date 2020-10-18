package com.fluentbuild.pcas.io

interface AtomicFile {

	fun exists(): Boolean

	fun readData(): ByteArray

	fun write(data: ByteArray)
}