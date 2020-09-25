package com.fluentbuild.pcas.host

import com.fluentbuild.pcas.async.Cancellable

class AndroidHostInfoWatcher: HostInfoWatcher {

    override val currentValue: HostInfo
        get() = TODO("Not yet implemented")

    override fun watch(consumer: (HostInfo) -> Unit): Cancellable {
        TODO("Not yet implemented")
    }
}