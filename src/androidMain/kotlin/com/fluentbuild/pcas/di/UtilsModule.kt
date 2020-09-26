package com.fluentbuild.pcas.di

import android.os.Handler
import com.fluentbuild.pcas.utils.ActionScheduler
import com.fluentbuild.pcas.utils.HandlerActionScheduler
import com.fluentbuild.pcas.utils.TimeProvider
import kotlinx.serialization.protobuf.ProtoBuf

class UtilsModule(
    private val handler: Handler
) {

    internal val protoBuf: ProtoBuf by lazy { ProtoBuf() }

    internal val actionScheduler: ActionScheduler by lazy { HandlerActionScheduler(handler) }

    internal val timeProvider: TimeProvider by lazy { TimeProvider() }
}