package com.fluentbuild.pcas

import com.fluentbuild.pcas.values.Observable

internal interface HostInfoObservable: Observable<HostInfo> {

    val currentValue: HostInfo
}