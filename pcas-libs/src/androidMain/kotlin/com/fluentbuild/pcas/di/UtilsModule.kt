package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.utils.TimeProvider
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.datetime.Clock
import java.security.SecureRandom

class UtilsModule {

    val protoBuf: ProtoBuf by lazy { ProtoBuf }

    val secureRandom by lazy {
        SecureRandom()
    }

    val timeProvider: TimeProvider by lazy { TimeProvider(Clock.System) }
}