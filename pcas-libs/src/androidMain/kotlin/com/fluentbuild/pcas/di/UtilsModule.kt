package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.utils.TimeProvider
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.datetime.Clock
import java.security.SecureRandom

internal class UtilsModule {

    val protoBuf = ProtoBuf

    // https://tersesystems.com/blog/2015/12/17/the-right-way-to-use-securerandom/
    val secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN")

    val timeProvider = TimeProvider(Clock.System)
}