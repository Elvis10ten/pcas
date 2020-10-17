package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.utils.TimeProviderJvm
import java.security.SecureRandom

internal open class UtilsModuleJvm: UtilsModule() {

	val secureRandom = SecureRandom()

	open val timeProvider = TimeProviderJvm()
}