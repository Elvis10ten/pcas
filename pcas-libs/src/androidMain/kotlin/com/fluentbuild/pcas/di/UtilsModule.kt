package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.utils.TimeProvider
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.datetime.Clock

class UtilsModule {

    val protoBuf: ProtoBuf by lazy { ProtoBuf }

    val timeProvider: TimeProvider by lazy { TimeProvider(Clock.System) }
}