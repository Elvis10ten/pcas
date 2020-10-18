package com.fluentbuild.pcas.host

import com.fluentbuild.pcas.values.Observable
import com.fluentbuild.pcas.values.Provider

internal interface HostInfoObservable: Observable<HostInfo>, Provider<HostInfo>