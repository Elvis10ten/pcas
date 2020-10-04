package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.utils.TimeProvider
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.datetime.Clock
import java.security.SecureRandom

internal class UtilsModule {

    val protoBuf = ProtoBuf

    val secureRandom = SecureRandom()

    val timeProvider = TimeProvider(Clock.System)
}