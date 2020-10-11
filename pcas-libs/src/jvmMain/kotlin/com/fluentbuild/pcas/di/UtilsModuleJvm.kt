package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.utils.TimeProviderJvm
import kotlinx.serialization.protobuf.ProtoBuf
import java.security.SecureRandom
import kotlin.random.Random

internal open class UtilsModuleJvm {

	val protoBuf = ProtoBuf

	val secureRandom = SecureRandom()

	val random = Random

	open val timeProvider = TimeProviderJvm()

}