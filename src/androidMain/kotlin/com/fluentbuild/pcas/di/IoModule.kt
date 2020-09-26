package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.io.AndroidMulticastChannel
import com.fluentbuild.pcas.io.AndroidUnicastChannel
import com.fluentbuild.pcas.io.MulticastChannel
import com.fluentbuild.pcas.io.UnicastChannel

class IoModule {

    internal val multicastChannel: MulticastChannel by lazy { AndroidMulticastChannel() }

    internal val unicastChannel: UnicastChannel by lazy { AndroidUnicastChannel() }
}