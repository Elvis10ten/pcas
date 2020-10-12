package com.fluentbuild.pcas.host

import com.fluentbuild.pcas.values.Observable

internal interface HostInfoObservable: Observable<HostInfo> {

    val currentValue: HostInfo
}