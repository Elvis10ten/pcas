package com.fluentbuild.pcas.host

import com.fluentbuild.pcas.async.Observable

internal interface HostInfoObservable: Observable<HostInfo> {

    val currentValue: HostInfo
}