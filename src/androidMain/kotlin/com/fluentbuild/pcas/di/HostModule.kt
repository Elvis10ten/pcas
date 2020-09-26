package com.fluentbuild.pcas.di

import com.fluentbuild.pcas.host.AndroidHostInfoWatcher
import com.fluentbuild.pcas.host.HostInfoWatcher

class HostModule {

    internal val selfHostInfoWatcher: HostInfoWatcher by lazy {
        AndroidHostInfoWatcher()
    }

}